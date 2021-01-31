package com.bartlomiejpluta.base.game.world.entity;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.world.movement.Direction;
import com.bartlomiejpluta.base.core.world.movement.MovableObject;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.Map;

public class Entity extends MovableObject {
   private static final Vector2i SPRITE_DIMENSION = new Vector2i(4, 4);
   private static final int DEFAULT_SPRITE = 0;
   private static final Map<Direction, Integer> SPRITE_ROWS = Map.of(
           Direction.DOWN, 0,
           Direction.LEFT, 1,
           Direction.RIGHT, 2,
           Direction.UP, 3
   );

   @Setter
   private int animationSpeed = 100;

   @Setter
   private Direction faceDirection;

   @Override
   public int getAnimationSpeed() {
      return 100;
   }

   @Override
   public Vector2i getSpriteSheetDimensions() {
      return SPRITE_DIMENSION;
   }

   @Override
   public boolean shouldAnimate() {
      return isMoving();
   }

   @Override
   public Vector2f[] getSpriteAnimationFramesPositions() {
      var row = SPRITE_ROWS.get(faceDirection);
      return new Vector2f[]{new Vector2f(0, row), new Vector2f(1, row), new Vector2f(2, row), new Vector2f(3, row)};
   }

   @Override
   protected void setDefaultAnimationFrame() {
      setAnimationFrame(new Vector2f(DEFAULT_SPRITE, SPRITE_ROWS.get(faceDirection)));
   }

   @Override
   public boolean move(Direction direction) {
      if(super.move(direction)) {
         faceDirection = direction;
         return true;
      }

      return false;
   }

   public void setMovementSlowness(int slowness) {
      framesToCrossOneTile = slowness;
   }

   public int getMovementSlowness() {
      return framesToCrossOneTile;
   }

   public Entity(Material material, Vector2f coordinateStepSize) {
      super(buildMesh(material), material, coordinateStepSize);
   }

   private static Mesh buildMesh(Material material) {
      var texture = material.getTexture();
      var spriteWidth = texture.getWidth() / (float) SPRITE_DIMENSION.x;
      var spriteHeight = texture.getHeight() / (float) SPRITE_DIMENSION.y;
      return Mesh.quad(spriteWidth, spriteHeight, spriteWidth / 2, spriteHeight);
   }
}
