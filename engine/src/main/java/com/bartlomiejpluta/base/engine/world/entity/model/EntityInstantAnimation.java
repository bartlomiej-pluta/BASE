package com.bartlomiejpluta.base.engine.world.entity.model;

import com.bartlomiejpluta.base.api.move.Direction;

public class EntityInstantAnimation {
   private static final int FIRST_FRAME = 0;

   private final DefaultEntity entity;
   private final int lastFrame;
   private final Direction faceDirectionOnFinish;
   private final Runnable onFinish;
   private boolean finished = false;

   EntityInstantAnimation(DefaultEntity entity, Direction faceDirectionOnFinish, Runnable onFinish) {
      this.entity = entity;
      this.lastFrame = entity.getMaterial().getTexture().getColumns() - 1;
      this.faceDirectionOnFinish = faceDirectionOnFinish;
      this.onFinish = onFinish;
   }

   public boolean updateFrame() {
      if (!finished && entity.currentFrame() == lastFrame) {
         finished = true;
         return false;
      }

      if (finished && entity.currentFrame() == FIRST_FRAME) {
         if (faceDirectionOnFinish != null) {
            entity.setFaceDirection(faceDirectionOnFinish);
         }

         if (onFinish != null) {
            onFinish.run();
         }

         return true;
      }

      return false;
   }
}
