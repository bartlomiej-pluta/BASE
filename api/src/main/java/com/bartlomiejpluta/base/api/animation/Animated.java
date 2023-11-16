package com.bartlomiejpluta.base.api.animation;

import com.bartlomiejpluta.base.internal.program.Updatable;

public interface Animated extends Updatable {
   boolean isAnimationEnabled();

   void setAnimationEnabled(boolean enabled);

   void enableAnimation();

   void disableAnimation();

   void toggleAnimationEnabled();

   float getAnimationSpeed();

   void setAnimationSpeed(float speed);

   void setAnimationFrame(int frame);
}
