package com.bartlomiejpluta.base.engine.world.entity.model;

import lombok.Getter;

import java.util.concurrent.CompletableFuture;

public class EntityInstantAnimation {
   public enum State {RUNNING, COMPLETED}

   private final DefaultEntity entity;
   private final int firstFrame;
   private final int lastFrame;

   private boolean finished = false;

   @Getter
   private final CompletableFuture<Void> future = new CompletableFuture<>();

   EntityInstantAnimation(DefaultEntity entity, int firstFrame) {
      this.entity = entity;
      this.firstFrame = firstFrame;
      this.lastFrame = entity.getMaterial().getTexture().getColumns() - 1;
   }

   public State update() {
      if (!finished && entity.currentFrame() == lastFrame) {
         finished = true;
         return State.RUNNING;
      }

      if (finished && entity.currentFrame() == firstFrame) {
         future.complete(null);

         return State.COMPLETED;
      }

      return State.RUNNING;
   }
}
