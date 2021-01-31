package com.bartlomiejpluta.base.game.world.entity;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.world.movement.Direction;
import com.bartlomiejpluta.base.core.world.movement.MovableObject;
import lombok.Setter;
import org.joml.Vector2f;

import java.util.Map;

public class Entity extends MovableObject {
   private final Map<Direction, Integer> spriteDirectionRows;
   private final int defaultSpriteColumn;

   @Setter
   private int animationSpeed = 100;

   @Setter
   private Direction faceDirection;

   @Override
   public int getAnimationSpeed() {
      return 100;
   }

   @Override
   public boolean shouldAnimate() {
      return isMoving();
   }

   @Override
   public Vector2f[] getSpriteAnimationFramesPositions() {
      var row = spriteDirectionRows.get(faceDirection);
      return new Vector2f[]{new Vector2f(0, row), new Vector2f(1, row), new Vector2f(2, row), new Vector2f(3, row)};
   }

   @Override
   protected void setDefaultAnimationFrame() {
      setAnimationFrame(new Vector2f(defaultSpriteColumn, spriteDirectionRows.get(faceDirection)));
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

   public Entity(Mesh mesh, Material material, Vector2f coordinateStepSize, EntitySpriteConfiguration configuration) {
      super(mesh, material, coordinateStepSize, configuration.getDimension().asVector());
      defaultSpriteColumn = configuration.getDefaultSpriteColumn();
      spriteDirectionRows = configuration.getSpriteDirectionRows();
   }
}
