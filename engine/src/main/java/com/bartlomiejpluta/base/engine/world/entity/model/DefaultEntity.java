package com.bartlomiejpluta.base.engine.world.entity.model;

import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.move.Movement;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.world.entity.manager.EntitySetManager;
import com.bartlomiejpluta.base.engine.world.movement.MovableSprite;
import com.bartlomiejpluta.base.util.math.MathUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;

import java.util.Map;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode(callSuper = true)
public class DefaultEntity extends MovableSprite implements Entity {
   private final EntitySetManager entitySetManager;
   private final Map<Direction, Integer> spriteDirectionRows;
   private final Map<Direction, Vector2fc> spriteDefaultRows;
   private final Vector2f entityScale = new Vector2f(1, 1);
   private Vector2fc entitySetSize;

   @Getter
   @Setter
   private int zIndex = 0;

   private int animationSpeed = 100;

   @Getter
   private Direction faceDirection;

   @Getter
   @Setter
   private boolean blocking;

   public DefaultEntity(Mesh mesh, EntitySetManager entitySetManager, Map<Direction, Integer> spriteDirectionRows, Map<Direction, Vector2fc> spriteDefaultRows, String entitySetUid) {
      super(mesh, entitySetManager.loadObject(requireNonNull(entitySetUid)));
      this.entitySetManager = entitySetManager;
      this.spriteDirectionRows = spriteDirectionRows;
      this.faceDirection = Direction.DOWN;
      this.spriteDefaultRows = spriteDefaultRows;

      this.entitySetSize = material.getTexture().getSpriteSize();
      super.setScale(entitySetSize.x() * entityScale.x, entitySetSize.y() * entityScale.y);
   }

   @Override
   public void changeEntitySet(String entitySetUid) {
      this.material = entitySetManager.loadObject(requireNonNull(entitySetUid));
      this.entitySetSize = material.getTexture().getSpriteSize();
      super.setScale(entitySetSize.x() * entityScale.x, entitySetSize.y() * entityScale.y);
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

   @Override
   public void setScaleX(float scaleX) {
      this.entityScale.x = scaleX;
      super.setScaleX(entitySetSize.x() * scaleX);
   }

   @Override
   public void setScaleY(float scaleY) {
      this.entityScale.y = scaleY;
      super.setScaleY(entitySetSize.y() * scaleY);
   }

   @Override
   public void setScale(float scale) {
      this.entityScale.x = scale;
      this.entityScale.y = scale;
      super.setScale(entitySetSize.x() * scale, entitySetSize.y() * scale);
   }

   @Override
   public void setScale(float scaleX, float scaleY) {
      this.entityScale.x = scaleX;
      this.entityScale.y = scaleY;
      super.setScale(entitySetSize.x() * scaleX, entitySetSize.y() * scaleY);
   }

   @Override
   public float getScaleX() {
      return entityScale.x;
   }

   @Override
   public float getScaleY() {
      return entityScale.y;
   }
}
