package com.bartlomiejpluta.base.engine.world.entity.model;

import com.bartlomiejpluta.base.api.entity.Direction;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.util.math.MathUtil;
import com.bartlomiejpluta.base.engine.world.entity.config.EntitySpriteConfiguration;
import com.bartlomiejpluta.base.engine.world.movement.MovableSprite;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
public class DefaultEntity extends MovableSprite implements Entity {
   private final Map<Direction, Integer> spriteDirectionRows;
   private final int defaultSpriteColumn;

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
      var frames = material.getTexture().getRows();
      var array = new Vector2f[frames];

      for (int column = 0; column < frames; ++column) {
         array[column] = new Vector2f(column, row);
      }

      return array;
   }

   @Override
   protected void setDefaultAnimationFrame() {
      material.setSpritePosition(new Vector2f(defaultSpriteColumn, spriteDirectionRows.get(faceDirection)));
   }

   @Override
   protected boolean move(Direction direction) {
      if (super.move(direction)) {
         faceDirection = direction;
         return true;
      }

      return false;
   }

   @Override
   public void setSpeed(float speed) {
      framesToCrossOneTile = (int) (1 / MathUtil.clamp(speed, Float.MIN_VALUE, 1.0));
   }

   @Override
   public void setAnimationSpeed(float speed) {
      animationSpeed = (int) (1 / MathUtil.clamp(speed, Float.MIN_VALUE, 1.0));
   }

   public DefaultEntity(Mesh mesh, Material material, EntitySpriteConfiguration configuration) {
      super(mesh, material);
      this.defaultSpriteColumn = configuration.getDefaultSpriteColumn();
      this.spriteDirectionRows = configuration.getSpriteDirectionRows();
      this.faceDirection = Direction.DOWN;
   }
}
