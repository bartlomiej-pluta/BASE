package com.bartlomiejpluta.base.engine.world.entity.model;

import com.bartlomiejpluta.base.api.game.entity.Direction;
import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.entity.Movement;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.util.math.MathUtil;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.world.movement.MovableSprite;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
public class DefaultEntity extends MovableSprite implements Entity {
   private final Map<Direction, Integer> spriteDirectionRows;
   private final Map<Direction, Vector2fc> spriteDefaultRows;

   private int animationSpeed = 100;

   @Getter
   private Direction faceDirection;

   @Getter
   @Setter
   private boolean blocking;

   public DefaultEntity(Mesh mesh, Material material, Map<Direction, Integer> spriteDirectionRows, Map<Direction, Vector2fc> spriteDefaultRows) {
      super(mesh, material);
      this.spriteDirectionRows = spriteDirectionRows;
      this.faceDirection = Direction.DOWN;
      this.spriteDefaultRows = spriteDefaultRows;
   }

   @Override
   public int getAnimationSpeed() {
      return animationSpeed;
   }

   @Override
   public boolean shouldAnimate() {
      return isMoving();
   }

   @Override
   public Vector2fc[] getSpriteAnimationFramesPositions() {
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
      material.setSpritePosition(spriteDefaultRows.get(faceDirection));
   }

   @Override
   protected boolean move(Movement movement) {
      if (super.move(movement)) {
         faceDirection = movement.getDirection();
         return true;
      }

      return false;
   }

   @Override
   public void setFaceDirection(Direction direction) {
      this.faceDirection = direction;
      setDefaultAnimationFrame();
   }

   @Override
   public void setAnimationSpeed(float speed) {
      animationSpeed = (int) (1 / MathUtil.clamp(speed, Float.MIN_VALUE, 1.0));
   }

   @Override
   public int chebyshevDistance(Entity other) {
      return chebyshevDistance(other.getCoordinates());
   }

   @Override
   public int manhattanDistance(Entity other) {
      return manhattanDistance(other.getCoordinates());
   }

   @Override
   public Direction getDirectionTowards(Entity target) {
      return Direction.ofVector(target.getCoordinates().sub(getCoordinates(), new Vector2i()));
   }

   @Override
   public void onAdd(ObjectLayer layer) {
      // Do nothing
   }

   @Override
   public void onRemove(ObjectLayer layer) {
      // Do nothing
   }
}
