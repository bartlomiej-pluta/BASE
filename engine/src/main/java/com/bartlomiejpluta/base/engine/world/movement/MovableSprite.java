package com.bartlomiejpluta.base.engine.world.movement;

import com.bartlomiejpluta.base.api.game.entity.Direction;
import com.bartlomiejpluta.base.api.game.entity.Movement;
import com.bartlomiejpluta.base.api.internal.logic.Updatable;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.world.animation.AnimatedSprite;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector2i;

import static java.lang.Math.abs;
import static java.lang.Math.max;

@EqualsAndHashCode(callSuper = true)
public abstract class MovableSprite extends AnimatedSprite implements Updatable {
   private final Vector2f coordinateStepSize = new Vector2f(0, 0);

   private int moveTime = 0;
   private Vector2f movementVector;

   @Getter
   private final Vector2i coordinates = new Vector2i(0, 0);

   @Getter
   private Movement movement;

   protected int framesToCrossOneTile = 1;

   public boolean isMoving() {
      return movement != null;
   }

   @Override
   public void update(float dt) {
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

      this.movement = movement;
      var speed = new Vector2f(coordinateStepSize).div(framesToCrossOneTile);
      movementVector = new Vector2f(movement.getDirection().x, movement.getDirection().y).mul(speed);
      moveTime = framesToCrossOneTile;

      return true;
   }

   public void setCoordinates(int x, int y) {
      coordinates.x = x;
      coordinates.y = y;
      setPosition((x + 0.5f) * coordinateStepSize.x, (y + 0.5f) * coordinateStepSize.y);
   }

   public void setCoordinates(Vector2i coordinates) {
      setCoordinates(coordinates.x, coordinates.y);
   }

   public void setStepSize(float x, float y) {
      coordinateStepSize.x = x;
      coordinateStepSize.y = y;
      setCoordinates(coordinates);
   }

   public int chebyshevDistance(Vector2i coordinates) {
      return max(abs(this.coordinates.x - coordinates.x), abs(this.coordinates.y - coordinates.y));
   }

   public int manhattanDistance(Vector2i coordinates) {
      return abs(this.coordinates.x - coordinates.x) + abs(this.coordinates.y - coordinates.y);
   }

   public MovableSprite(Mesh mesh, Material material) {
      super(mesh, material);
      setCoordinates(0, 0);
   }
}
