package com.bartlomiejpluta.base.lib.ai;

import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Random;

@RequiredArgsConstructor
public class RunawayAI<N extends NPC, T extends Entity> implements AI {
   @NonNull
   private final N npc;

   @NonNull
   private final T character;

   private final Random random = new Random();

   @Override
   public void nextActivity(ObjectLayer layer, float dt) {
      if (npc.isMoving()) {
         return;
      }

      var direction = npc.getDirectionTowards(character).opposite();
      if (tryToMove(layer, direction)) {
         return;
      }

      var perpendiculars = direction.perpendiculars();
      var first = random.nextInt(2);
      var second = 1 - first;

      if (tryToMove(layer, perpendiculars[first])) {
         return;
      }

      tryToMove(layer, perpendiculars[second]);
   }

   private boolean tryToMove(ObjectLayer layer, Direction direction) {
      var movement = npc.prepareMovement(direction);
      if (layer.isTileReachable(movement.getTo())) {
         layer.pushMovement(movement);
         return true;
      }

      return false;
   }
}