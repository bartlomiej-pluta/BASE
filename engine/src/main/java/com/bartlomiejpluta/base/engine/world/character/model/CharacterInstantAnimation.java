package com.bartlomiejpluta.base.engine.world.character.model;

import lombok.Getter;

import java.util.concurrent.CompletableFuture;

public class CharacterInstantAnimation {
   public enum State {RUNNING, COMPLETED}

   private final DefaultCharacter character;
   private final int firstFrame;
   private final int lastFrame;

   private boolean finished = false;

   @Getter
   private final CompletableFuture<Void> future = new CompletableFuture<>();

   CharacterInstantAnimation(DefaultCharacter character, int firstFrame) {
      this.character = character;
      this.firstFrame = firstFrame;
      this.lastFrame = character.getMaterial().getTexture().getColumns() - 1;
   }

   public State update() {
      if (!finished && character.currentFrame() == lastFrame) {
         finished = true;
         return State.RUNNING;
      }

      if (finished && character.currentFrame() == firstFrame) {
         future.complete(null);

         return State.COMPLETED;
      }

      return State.RUNNING;
   }
}
