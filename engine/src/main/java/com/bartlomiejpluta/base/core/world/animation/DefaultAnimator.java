package com.bartlomiejpluta.base.core.world.animation;

import org.springframework.stereotype.Component;

@Component
public class DefaultAnimator implements Animator {

   @Override
   public void animate(AnimationableObject object) {
      if(object.shouldAnimate()) {
         var positions = object.getSpriteAnimationFramesPositions();
         var delay = object.getAnimationSpeed();
         var currentPosition = (int) (System.currentTimeMillis() % (positions.length * delay)) / delay;
         var current = positions[currentPosition];
         object.setAnimationFrame(current);
      }
   }
}
