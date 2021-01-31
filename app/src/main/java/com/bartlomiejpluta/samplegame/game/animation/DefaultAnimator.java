package com.bartlomiejpluta.samplegame.game.animation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultAnimator implements Animator {

   @Override
   public void animate(AnimationableObject[] objects) {
      for (var object : objects) {
         animate(object);
      }
   }

   private void animate(AnimationableObject object) {
      if(object.shouldAnimate()) {
         var positions = object.getSpriteAnimationFramesPositions();
         var delay = object.getAnimationSpeed();
         var currentPosition = (int) (System.currentTimeMillis() % (positions.length * delay)) / delay;
         var spriteSize = object.getMaterial().getSpriteSize();
         var current = positions[currentPosition];
         object.getMaterial().setSpritePosition(spriteSize.x * current.x, spriteSize.y * current.y);
      }
   }
}
