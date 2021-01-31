package com.bartlomiejpluta.base.game.world.movement;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.world.animation.AnimationableObject;
import com.bartlomiejpluta.base.game.logic.Updatable;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;
import org.joml.Vector2i;

@Slf4j
public abstract class MoveableObject extends AnimationableObject implements Updatable {
   private static final Vector2i SPRITE_DIMENSION = new Vector2i(4, 4);

   private final Vector2f coordinateStepSize;
   private Movement movement;

   @Getter
   private final Vector2i coordinates = new Vector2i(0, 0);

   @Getter
   @Setter
   private Direction faceDirection = Direction.DOWN;

   @Getter
   @Setter
   private int slowness;

   @Setter
   private int animationSpeed;

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
      if(movement != null) {
         var dS = movement.getMovementVector();
         if(dS != null) {
            movePosition(dS);
         } else {
            adjustCoordinates();
            movement = null;
         }
      }
   }

   private void adjustCoordinates() {
      var position = new Vector2f(getPosition());
      setCoordinates(new Vector2i((int) (position.x / coordinateStepSize.x), (int) (position.y / coordinateStepSize.y)));
   }

   public void move(Direction direction) {
      if(this.movement != null) {
         return;
      }
      setFaceDirection(direction);
      this.movement = new Movement(direction, coordinateStepSize, 50);
   }

   public MoveableObject setCoordinates(int x, int y) {
      coordinates.x = x;
      coordinates.y = y;
      setPosition((x + 0.5f) * coordinateStepSize.x, (y + 0.5f) * coordinateStepSize.y);
      return this;
   }

   public MoveableObject setCoordinates(Vector2i coordinates) {
      return setCoordinates(coordinates.x, coordinates.y);
   }

   @Override
   public Vector2i getSpriteSheetDimensions() {
      return SPRITE_DIMENSION;
   }

   @Override
   public Vector2f[] getSpriteAnimationFramesPositions() {
      var row = switch (faceDirection) {
         case DOWN -> 0;
         case LEFT -> 1;
         case RIGHT -> 2;
         case UP -> 3;
      };

      return new Vector2f[]{new Vector2f(0, row), new Vector2f(1, row), new Vector2f(2, row), new Vector2f(3, row)};
   }

   public MoveableObject(Material material, Vector2f coordinateStepSize, float scale) {
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
