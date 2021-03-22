package com.bartlomiejpluta.base.lib.animation;

import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.api.animation.AnimationDelegate;
import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.internal.render.ShaderManager;

public class DelayedAnimation extends AnimationDelegate {
   private final int delay;
   private int accumulator = 0;

   public DelayedAnimation(Animation animation, int delay) {
      super(animation);
      this.delay = delay;
   }

   @Override
   public void update(float dt) {
      if (accumulator >= delay) {
         super.update(dt);
      } else {
         accumulator += dt * 1000;
      }
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      if (accumulator >= delay) {
         super.render(screen, camera, shaderManager);
      }
   }
}
