package com.bartlomiejpluta.base.engine.world.movement;

import com.bartlomiejpluta.base.api.move.Movable;
import com.bartlomiejpluta.base.api.move.Movement;
import com.bartlomiejpluta.base.engine.core.engine.DefaultGameEngine;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.world.animation.model.AnimatedSprite;
import com.bartlomiejpluta.base.internal.logic.Updatable;
import com.bartlomiejpluta.base.util.math.Distance;
import com.bartlomiejpluta.base.util.math.MathUtil;
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
   private int moveTime = 0;
   private Vector2f movementVector;
   private int framesToCrossOneTile = 1;

   @Getter
   private Movement movement;

   public MovableSprite(Mesh mesh, Material material) {
      super(mesh, material);
   }

   @Override
   public boolean isMoving() {
      return movement != null;
   }

   @Override
   public void setSpeed(float speed) {
      framesToCrossOneTile = (int) (1 / MathUtil.clamp(speed / DefaultGameEngine.TARGET_UPS, Float.MIN_VALUE, 1.0));
   }

   protected abstract void setDefaultAnimationFrame();

   private void adjustCoordinates() {
      setCoordinates(movement.getTo());
   }

   @Override
   public boolean move(Movement movement) {
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

   @Override
   public void abortMove() {
      setCoordinates(movement.getFrom());
      setDefaultAnimationFrame();
      movementVector = null;
      movement = null;
   }

   @Override
   public void update(float dt) {
      super.update(dt);

      if (movement != null) {
         if (moveTime > 0) {
            moveTime -= dt;
            movePosition(movementVector);
         } else {
            adjustCoordinates();
            setDefaultAnimationFrame();
            movement.onFinish();
            movementVector = null;
            movement = null;
         }
      }
   }
}
