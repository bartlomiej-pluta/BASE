package com.bartlomiejpluta.base.engine.world.movement;

import com.bartlomiejpluta.base.api.game.move.Direction;
import com.bartlomiejpluta.base.api.game.move.Movable;
import com.bartlomiejpluta.base.api.game.move.Movement;
import com.bartlomiejpluta.base.api.internal.logic.Updatable;
import com.bartlomiejpluta.base.api.util.math.MathUtil;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.world.animation.model.AnimatedSprite;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import static java.lang.Math.abs;
import static java.lang.Math.max;

@EqualsAndHashCode(callSuper = true)
public abstract class MovableSprite extends AnimatedSprite implements Movable, Updatable {
   private final Vector2f coordinateStepSize = new Vector2f(0, 0);

   private final Vector2i coordinates = new Vector2i(0, 0);
   private final Vector2f positionOffset = new Vector2f(0, 0);
   private int moveTime = 0;
   private Vector2f movementVector;
   private int framesToCrossOneTile = 1;

   @Getter
   private Movement movement;

   public Vector2ic getCoordinates() {
      return coordinates;
   }

   @Override
   public boolean isMoving() {
      return movement != null;
   }

   @Override
   public void setSpeed(float speed) {
      framesToCrossOneTile = (int) (1 / MathUtil.clamp(speed, Float.MIN_VALUE, 1.0));
   }

   @Override
   public void update(float dt) {
      super.update(dt);

      if (movement != null) {
         if (moveTime > 0) {
            --moveTime;
            movePosition(movementVector);
         } else {
            adjustCoordinates();
            setDefaultAnimationFrame();
            movementVector = null;
            movement = null;
         }
      }
   }

   protected abstract void setDefaultAnimationFrame();

   private void adjustCoordinates() {
      setCoordinates(movement.getTo());
   }

   @Override
   public Movement prepareMovement(Direction direction) {
      return new DefaultMovement(this, direction);
   }

   protected boolean move(Movement movement) {
      if (this.movement != null) {
         return false;
      }

      if (!movement.getFrom().equals(coordinates)) {
         return false;
      }

      var direction = movement.getDirection().vector;

      this.movement = movement;
      var speed = new Vector2f(coordinateStepSize).div(framesToCrossOneTile);
      movementVector = new Vector2f(speed.x * direction.x(), speed.y * direction.y());
      moveTime = framesToCrossOneTile;

      return true;
   }

   public void setCoordinates(int x, int y) {
      coordinates.x = x;
      coordinates.y = y;
      super.setPosition((x + 0.5f) * coordinateStepSize.x + positionOffset.x, (y + 0.5f) * coordinateStepSize.y + positionOffset.y);
   }

   @Override
   public void setPosition(float x, float y) {
      super.setPosition(x, y);
      coordinates.x = (int) ((x - positionOffset.x) / coordinateStepSize.x);
      coordinates.y = (int) ((y - positionOffset.y) / coordinateStepSize.y);
   }

   @Override
   public void setPosition(Vector2fc position) {
      super.setPosition(position);
      coordinates.x = (int) ((position.x() - positionOffset.x) / coordinateStepSize.x);
      coordinates.y = (int) ((position.y() - positionOffset.y) / coordinateStepSize.y);
   }

   public void setCoordinates(Vector2ic coordinates) {
      setCoordinates(coordinates.x(), coordinates.y());
   }

   public void setStepSize(float x, float y) {
      coordinateStepSize.x = x;
      coordinateStepSize.y = y;

      if (!position.equals(0, 0)) {
         setPosition(position);
      } else {
         setCoordinates(coordinates);
      }
   }

   @Override
   public void setPositionOffset(Vector2fc offset) {
      this.positionOffset.x = offset.x();
      this.positionOffset.y = offset.y();
   }

   @Override
   public void setPositionOffset(float offsetX, float offsetY) {
      this.positionOffset.x = offsetX;
      this.positionOffset.y = offsetY;
   }

   public int chebyshevDistance(Vector2ic coordinates) {
      return max(abs(this.coordinates.x - coordinates.x()), abs(this.coordinates.y - coordinates.y()));
   }

   public int manhattanDistance(Vector2ic coordinates) {
      return abs(this.coordinates.x - coordinates.x()) + abs(this.coordinates.y - coordinates.y());
   }

   public MovableSprite(Mesh mesh, Material material) {
      super(mesh, material);
      setCoordinates(0, 0);
   }
}
