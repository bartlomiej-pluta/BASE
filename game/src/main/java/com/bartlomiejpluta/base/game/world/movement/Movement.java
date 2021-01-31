package com.bartlomiejpluta.base.game.world.movement;

import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;

@Slf4j
public class Movement {
   private final Vector2f dS;
   private int moveTime;

   public Vector2f getMovementVector() {
      if(moveTime > 0) {
         --moveTime;

         return new Vector2f(dS);
      }

      return null;
   }

   public Movement(Direction direction, Vector2f tileSize, int framesToCrossOneTile) {
      var speed = new Vector2f(tileSize).div(framesToCrossOneTile);
      dS = direction.asFloatVector().mul(speed);
      moveTime = framesToCrossOneTile;
   }

//   public Movement(Direction direction, float tileWidth) {
//      var speed = tileWidth / (float) framesToCrossOneTile;
//
//      var offsetCoordinates = switch (direction) {
//         case UP -> new Vector2f(0, -1);
//         case DOWN -> new Vector2f(0, 1);
//         case LEFT -> new Vector2f(-1, 0);
//         case RIGHT -> new Vector2f(1, 0);
//      };
//
//      dS = offsetCoordinates.mul(speed);
//
//      moveTime = framesToCrossOneTile;
//   }


//   private static final int framesToCrossOneTile = 28;
//
//   @Getter
//   private final Vector2i targetCoordinates;
//
//   private final Vector2f targetPosition;
//   private final Vector2f dS;
//   private int moveTime;
//
//   public Vector2f getNextFrameOffset(Vector2f currentPosition, float tolerance) {
//      if(moveTime > 0) {
//         moveTime--;
////               log.info("dS: [{}, {}]", dS.x, dS.y);
//         return new Vector2f(dS);
//      }
//
//      return null;
////      var distance = new Vector2f(dS).add(currentPosition).distance(targetPosition);
//
////      return (distance > 0) ? new Vector2f(dS) : null;
//   }
//
////   public Movement(Direction direction, float speed, Vector2i currentCoordinates, Vector2f currentPosition, Vector2f coordinateStepSize) {
////      var offsetCoordinates = switch (direction) {
////         case UP -> new Vector2i(0, -1);
////         case DOWN -> new Vector2i(0, 1);
////         case LEFT -> new Vector2i(-1, 0);
////         case RIGHT -> new Vector2i(1, 0);
////      };
////
////      moveTime = framesToCrossOneTile;
////      var _speed = coordinateStepSize.x / moveTime;
////      targetCoordinates = new Vector2i(currentCoordinates).add(offsetCoordinates);
////      var offsetPosition = new Vector2f(offsetCoordinates).mul(coordinateStepSize);
////      targetPosition = new Vector2f(currentPosition).add(offsetPosition);
////      dS = new Vector2f(offsetCoordinates).mul(_speed);
////      log.info("{}", _speed);
////   }
}
