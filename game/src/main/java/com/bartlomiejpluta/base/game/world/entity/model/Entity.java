package com.bartlomiejpluta.base.game.world.entity.model;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.world.movement.Direction;
import com.bartlomiejpluta.base.core.world.movement.MovableObject;
import com.bartlomiejpluta.base.game.world.entity.config.EntitySpriteConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
public class Entity extends MovableObject {
   private final Map<Direction, Integer> spriteDirectionRows;
   private final int defaultSpriteColumn;

   @Setter
   private int animationSpeed = 100;

   @Setter
   @Getter
   private Direction faceDirection;

   @Override
   public int getAnimationSpeed() {
      return animationSpeed;
   }

   @Override
   public boolean shouldAnimate() {
      return isMoving();
   }

   @Override
   public Vector2f[] getSpriteAnimationFramesPositions() {
      var row = spriteDirectionRows.get(faceDirection);
      var frames = spriteSheetDimension.y;
      var array = new Vector2f[frames];

      for(int column=0; column<frames; ++column) {
         array[column] = new Vector2f(column, row);
      }

      return array;
   }

   @Override
   protected void setDefaultAnimationFrame() {
      setAnimationFrame(new Vector2f(defaultSpriteColumn, spriteDirectionRows.get(faceDirection)));
   }

   @Override
   protected boolean move(Direction direction) {
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
      this.defaultSpriteColumn = configuration.getDefaultSpriteColumn();
      this.spriteDirectionRows = configuration.getSpriteDirectionRows();
      this.faceDirection = Direction.DOWN;
   }
}