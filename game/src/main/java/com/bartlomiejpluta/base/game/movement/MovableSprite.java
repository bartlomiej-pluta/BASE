package com.bartlomiejpluta.base.game.movement;

import com.bartlomiejpluta.base.api.entity.Direction;
import com.bartlomiejpluta.base.api.entity.Movement;
import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.logic.Updatable;
import com.bartlomiejpluta.base.game.animation.AnimatedSprite;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector2i;

@EqualsAndHashCode(callSuper = true)
public abstract class MovableSprite extends AnimatedSprite implements Updatable {
   private final Vector2f coordinateStepSize = new Vector2f(0, 0);

   private int moveTime = 0;
   private Vector2f movementVector;

   @Getter
   private final Vector2i coordinates = new Vector2i(0, 0);

   protected int framesToCrossOneTile = 1;

   public boolean isMoving() {
      return movementVector != null;
   }

   @Override
   public void update(float dt) {
      if (movementVector != null) {
         if (moveTime > 0) {
            --moveTime;
            movePosition(movementVector);
         } else {
            adjustCoordinates();
            setDefaultAnimationFrame();
            movementVector = null;
         }
      }
   }

   protected abstract void setDefaultAnimationFrame();

   private void adjustCoordinates() {
      var position = new Vector2f(getPosition());
      setCoordinates(new Vector2i((int) (position.x / coordinateStepSize.x), (int) (position.y / coordinateStepSize.y)));
   }

   public Movement prepareMovement(Direction direction) {
      return new DefaultMovement(this, direction);
   }

   protected boolean move(Direction direction) {
      if (this.movementVector != null) {
         return false;
      }

      var speed = new Vector2f(coordinateStepSize).div(framesToCrossOneTile);
      movementVector = new Vector2f(direction.x, direction.y).mul(speed);
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

   public MovableSprite(Mesh mesh, Material material) {
      super(mesh, material);
      setCoordinates(0, 0);
   }
}
