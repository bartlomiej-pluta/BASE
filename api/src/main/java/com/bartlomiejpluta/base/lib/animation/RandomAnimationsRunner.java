package com.bartlomiejpluta.base.lib.animation;

import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.move.Movable;
import lombok.NonNull;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.joml.Vector2fc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

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

   private float animationSpeed = 3f;
   private RealDistribution animationSpeedDistribution;

   private float rotation = 0f;
   private RealDistribution rotationDistribution;

   private float offsetX = 0;
   private float offsetY = 0;

   private BiConsumer<Integer, Animation> customizer;

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

   public RandomAnimationsRunner animationSpeed(float speed) {
      this.animationSpeed = speed;
      return this;
   }

   public RandomAnimationsRunner uAnimationSpeed(float min, float max) {
      this.animationSpeedDistribution = new UniformRealDistribution(min, max);
      return this;
   }

   public RandomAnimationsRunner nAnimationSpeed(float mean, float sd) {
      this.animationSpeedDistribution = new NormalDistribution(mean, sd);
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

   public RandomAnimationsRunner offset(float x, float y) {
      this.offsetX = x;
      this.offsetY = y;
      return this;
   }

   public RandomAnimationsRunner customize(@NonNull BiConsumer<Integer, Animation> customizer) {
      this.customizer = customizer;
      return this;
   }

   @Override
   public CompletableFuture<Void> run(Context context, Layer layer, Vector2fc origin) {
      var futures = new CompletableFuture[count];

      for (int i = 0; i < count; ++i) {
         var animation = context.createAnimation(animationUids.get(random.nextInt(animationUids.size())));

         if (rangeDistribution != null) {
            animation.setPosition(origin.x() + (float) rangeDistribution.sample(), origin.y() + (float) rangeDistribution.sample());
         } else {
            animation.setPosition(origin.x() + rangeX, origin.y() + rangeY);
         }

         animation.setPositionOffset(offsetX, offsetY);
         animation.setScale(scaleDistribution != null ? (float) scaleDistribution.sample() : scale);
         animation.setAnimationSpeed(animationSpeedDistribution != null ? (float) animationSpeedDistribution.sample() : animationSpeed);
         animation.setRotation(rotationDistribution != null ? (float) rotationDistribution.sample() : rotation);

         layer.pushAnimation(new DelayedAnimation(animation, (int) (delayDistribution != null ? delayDistribution.sample() : delay)));

         if (customizer != null) {
            customizer.accept(i, animation);
         }

         futures[i] = animation.getFuture();
      }

      return CompletableFuture.allOf(futures);
   }

   @Override
   public CompletableFuture<Void> run(Context context, Layer layer, Movable origin) {
      var futures = new CompletableFuture[count];

      for (int i = 0; i < count; ++i) {
         var animation = context.createAnimation(animationUids.get(random.nextInt(animationUids.size())));

         var position = origin.getPosition();
         var offset = origin.getPositionOffset();

         if (rangeDistribution != null) {
            animation.setPosition(position.x() - offset.x() + (float) rangeDistribution.sample(), position.y() - offset.y() + (float) rangeDistribution.sample());
         } else {
            animation.setPosition(position.x() - offset.x() + rangeX, position.y() - offset.y() + rangeY);
         }

         animation.setPositionOffset(offsetX, offsetY);
         animation.setScale(scaleDistribution != null ? (float) scaleDistribution.sample() : scale);
         animation.setAnimationSpeed(animationSpeedDistribution != null ? (float) animationSpeedDistribution.sample() : animationSpeed);
         animation.setRotation(rotationDistribution != null ? (float) rotationDistribution.sample() : rotation);

         layer.pushAnimation(new DelayedAnimation(animation, (int) (delayDistribution != null ? delayDistribution.sample() : delay)));

         if (customizer != null) {
            customizer.accept(i, animation);
         }

         futures[i] = animation.getFuture();
      }

      return CompletableFuture.allOf(futures);
   }
}
