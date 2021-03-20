package com.bartlomiejpluta.base.api.game.animation;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.map.layer.base.Layer;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.joml.Vector2fc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.max;

public class RandomAnimationsRunner implements AnimationRunner {
   private final Random random = new Random();
   private final List<String> animationUids = new ArrayList<>();
   private final int count;

   private float scale = 1.0f;
   private RealDistribution scaleDistribution;

   private float rangeX = 0f;
   private float rangeY = 0f;
   private RealDistribution rangeDistribution;

   private float delay = 0f;
   private RealDistribution delayDistribution;

   private float speed = 0.05f;
   private RealDistribution speedDistribution;

   private float rotation = 0f;
   private RealDistribution rotationDistribution;

   public RandomAnimationsRunner(int count) {
      this.count = max(count, 0);
   }

   public RandomAnimationsRunner with(String animationUid) {
      animationUids.add(animationUid);
      return this;
   }

   public RandomAnimationsRunner scale(float scale) {
      this.scale = scale;
      return this;
   }

   public RandomAnimationsRunner uScale(float min, float max) {
      this.scaleDistribution = new UniformRealDistribution(min, max);
      return this;
   }

   public RandomAnimationsRunner nScale(float mean, float sd) {
      this.scaleDistribution = new NormalDistribution(mean, sd);
      return this;
   }

   public RandomAnimationsRunner range(float rangeX, float rangeY) {
      this.rangeX = rangeX;
      this.rangeY = rangeY;
      return this;
   }

   public RandomAnimationsRunner uRange(float min, float max) {
      this.rangeDistribution = new UniformRealDistribution(min, max);
      return this;
   }

   public RandomAnimationsRunner nRange(float mean, float sd) {
      this.rangeDistribution = new NormalDistribution(mean, sd);
      return this;
   }

   public RandomAnimationsRunner speed(float speed) {
      this.speed = speed;
      return this;
   }

   public RandomAnimationsRunner uSpeed(float min, float max) {
      this.speedDistribution = new UniformRealDistribution(min, max);
      return this;
   }

   public RandomAnimationsRunner nSpeed(float mean, float sd) {
      this.speedDistribution = new NormalDistribution(mean, sd);
      return this;
   }

   public RandomAnimationsRunner delay(float delay) {
      this.delay = delay;
      return this;
   }

   public RandomAnimationsRunner uDelay(float min, float max) {
      this.delayDistribution = new UniformRealDistribution(min, max);
      return this;
   }

   public RandomAnimationsRunner nDelay(float mean, float sd) {
      this.delayDistribution = new NormalDistribution(mean, sd);
      return this;
   }

   public RandomAnimationsRunner rotation(float rotation) {
      this.rotation = rotation;
      return this;
   }

   public RandomAnimationsRunner uRotation(float min, float max) {
      this.rotationDistribution = new UniformRealDistribution(min, max);
      return this;
   }

   public RandomAnimationsRunner nRotation(float mean, float sd) {
      this.rotationDistribution = new NormalDistribution(mean, sd);
      return this;
   }

   @Override
   public void run(Context context, Layer layer, Vector2fc origin) {
      for (int i = 0; i < count; ++i) {
         var animation = context.createAnimation(animationUids.get(random.nextInt(animationUids.size())));

         if (rangeDistribution != null) {
            animation.setPosition(origin.x() + (float) rangeDistribution.sample(), origin.y() + (float) rangeDistribution.sample());
         } else {
            animation.setPosition(origin.x() + rangeX, origin.y() + rangeY);
         }

         animation.setScale(scaleDistribution != null ? (float) scaleDistribution.sample() : scale);
         animation.setAnimationSpeed(speedDistribution != null ? (float) speedDistribution.sample() : speed);
         animation.setRotation(rotationDistribution != null ? (float) rotationDistribution.sample() : rotation);

         layer.pushAnimation(new DelayedAnimation(animation, (int) (delayDistribution != null ? delayDistribution.sample() : delay)));
      }
   }
}
