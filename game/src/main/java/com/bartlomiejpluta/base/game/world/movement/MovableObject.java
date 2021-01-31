package com.bartlomiejpluta.base.game.world.movement;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.world.animation.AnimationableObject;
import com.bartlomiejpluta.base.game.logic.Updatable;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.Map;


public abstract class MovableObject extends AnimationableObject implements Updatable {
   private static final Vector2i SPRITE_DIMENSION = new Vector2i(4, 4);
   private static final int DEFAULT_SPRITE = 0;
   private static final Map<Direction, Integer> SPRITE_ROWS = Map.of(
           Direction.DOWN, 0,
           Direction.LEFT, 1,
           Direction.RIGHT, 2,
           Direction.UP, 3
   );

   private final Vector2f coordinateStepSize;
   private Movement movement;

   @Getter
   private final Vector2i coordinates = new Vector2i(0, 0);

   @Getter
   @Setter
   private Direction faceDirection = Direction.DOWN;

   @Getter
   @Setter
   private int framesToCrossOneTile = 1;

   @Setter
   private int animationSpeed = 100;

   @Override
   public int getAnimationSpeed() {
      return animationSpeed;
   }

   @Override
   public boolean shouldAnimate() {
      return movement != null;
   }

   @Override
   public void update(float dt) {
      if (movement != null) {
         var dS = movement.getMovementVector();
         if (dS != null) {
            movePosition(dS);
         } else {
            adjustCoordinates();
            movement = null;
            setAnimationFrame(new Vector2f(DEFAULT_SPRITE, SPRITE_ROWS.get(faceDirection)));
         }
      }
   }

   private void adjustCoordinates() {
      var position = new Vector2f(getPosition());
      setCoordinates(new Vector2i((int) (position.x / coordinateStepSize.x), (int) (position.y / coordinateStepSize.y)));
   }

   public void move(Direction direction) {
      if (this.movement != null) {
         return;
      }
      setFaceDirection(direction);
      this.movement = new Movement(direction, coordinateStepSize, framesToCrossOneTile);
   }

   public MovableObject setCoordinates(int x, int y) {
      coordinates.x = x;
      coordinates.y = y;
      setPosition((x + 0.5f) * coordinateStepSize.x, (y + 0.5f) * coordinateStepSize.y);
      return this;
   }

   public MovableObject setCoordinates(Vector2i coordinates) {
      return setCoordinates(coordinates.x, coordinates.y);
   }

   @Override
   public Vector2i getSpriteSheetDimensions() {
      return SPRITE_DIMENSION;
   }

   @Override
   public Vector2f[] getSpriteAnimationFramesPositions() {
      var row = SPRITE_ROWS.get(faceDirection);
      return new Vector2f[]{new Vector2f(0, row), new Vector2f(1, row), new Vector2f(2, row), new Vector2f(3, row)};
   }

   public MovableObject(Material material, Vector2f coordinateStepSize, float scale) {
      super(buildMesh(material), material);
      this.coordinateStepSize = coordinateStepSize;
      this.setScale(scale);
      setCoordinates(0, 0);
   }

   private static Mesh buildMesh(Material material) {
      var texture = material.getTexture();
      var spriteWidth = texture.getWidth() / (float) SPRITE_DIMENSION.x;
      var spriteHeight = texture.getHeight() / (float) SPRITE_DIMENSION.y;
      return Mesh.quad(spriteWidth, spriteHeight, spriteWidth / 2, spriteHeight);
   }
}
