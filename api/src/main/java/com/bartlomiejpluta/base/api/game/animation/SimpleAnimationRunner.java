package com.bartlomiejpluta.base.api.game.animation;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.map.layer.base.Layer;
import org.joml.Vector2fc;

public class SimpleAnimationRunner implements AnimationRunner {
   private final String animationUid;

   private Integer repeat = 1;
   private float scale = 1.0f;
   private int delay = 0;
   private float speed = 0.05f;
   private float rotation = 0f;

   public SimpleAnimationRunner(String animationUid) {
      this.animationUid = animationUid;
   }

   public SimpleAnimationRunner repeat(int n) {
      this.repeat = n;
      return this;
   }

   public SimpleAnimationRunner infinite() {
      this.repeat = null;
      return this;
   }

   public SimpleAnimationRunner scale(float scale) {
      this.scale = scale;
      return this;
   }

   public SimpleAnimationRunner delay(int delay) {
      this.delay = delay;
      return this;
   }

   public SimpleAnimationRunner speed(float speed) {
      this.speed = speed;
      return this;
   }

   public SimpleAnimationRunner rotation(float rotation) {
      this.rotation = rotation;
      return this;
   }

   @Override
   public void run(Context context, Layer layer, Vector2fc origin) {
      var animation = new DelayedAnimation(context.createAnimation(animationUid), delay);
      animation.setPosition(origin);
      layer.pushAnimation(animation);
   }
}
