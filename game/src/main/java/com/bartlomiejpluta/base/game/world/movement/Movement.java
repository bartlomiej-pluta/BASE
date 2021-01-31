package com.bartlomiejpluta.base.game.world.movement;

import org.joml.Vector2f;


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
}
