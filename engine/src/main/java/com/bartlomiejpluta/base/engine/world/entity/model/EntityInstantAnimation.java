package com.bartlomiejpluta.base.engine.world.entity.model;

import com.bartlomiejpluta.base.api.move.Direction;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;

public class EntityInstantAnimation {

   private final DefaultEntity entity;
   private final int firstFrame;
   private final int lastFrame;
   private final Direction faceDirectionOnFinish;
   private boolean finished = false;

   @Getter
   private final CompletableFuture<Void> future = new CompletableFuture<>();

   EntityInstantAnimation(DefaultEntity entity, int firstFrame) {
      this(entity, firstFrame, null);
   }

   EntityInstantAnimation(DefaultEntity entity, int firstFrame, Direction faceDirectionOnFinish) {
      this.entity = entity;
      this.firstFrame = firstFrame;
      this.lastFrame = entity.getMaterial().getTexture().getColumns() - 1;
      this.faceDirectionOnFinish = faceDirectionOnFinish;
   }

   public boolean updateFrame() {
      if (!finished && entity.currentFrame() == lastFrame) {
         finished = true;
         return false;
      }

      if (finished && entity.currentFrame() == firstFrame) {
         if (faceDirectionOnFinish != null) {
            entity.setFaceDirection(faceDirectionOnFinish);
         }

         future.complete(null);

         return true;
      }

      return false;
   }
}
