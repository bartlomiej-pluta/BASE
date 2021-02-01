package com.bartlomiejpluta.base.core.world.movement;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.logic.Updatable;
import com.bartlomiejpluta.base.core.world.animation.AnimationableObject;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector2i;


public abstract class MovableObject extends AnimationableObject implements Updatable {
   private final Vector2f coordinateStepSize;

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
      if(movementVector != null) {
         if(moveTime > 0) {
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
      return new Movement(this, direction);
   }

   protected boolean move(Direction direction) {
      if (this.movementVector != null) {
         return false;
      }

      var speed = new Vector2f(coordinateStepSize).div(framesToCrossOneTile);
      movementVector = direction.asFloatVector().mul(speed);
      moveTime = framesToCrossOneTile;

      return true;
   }

   public MovableObject setCoordinates(int x, int y) {
      coordinates.x = x;
      coordinates.y = y;
      setPosition((x + 0.5f) * coordinateStepSize.x, (y + 0.5f) * coordinateStepSize.y);
      return this;
   }

   public MovableObject setCoordinates(Vector2i coordinates) {
      return setCoordinates(coordinates.x, coordinates.y);
   }

   public MovableObject(Mesh mesh, Material material, Vector2f coordinateStepSize, Vector2i spriteSheetDimensions) {
      super(mesh, material, spriteSheetDimensions);
      this.coordinateStepSize = coordinateStepSize;
      setCoordinates(0, 0);
   }
}
