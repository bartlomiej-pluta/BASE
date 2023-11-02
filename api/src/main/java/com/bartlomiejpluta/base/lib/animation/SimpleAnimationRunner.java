package com.bartlomiejpluta.base.lib.animation;

import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.move.Movable;
import com.bartlomiejpluta.base.util.path.Path;
import org.joml.Vector2fc;

import java.util.concurrent.CompletableFuture;

public class SimpleAnimationRunner implements AnimationRunner {
   private final String animationUid;

   private Integer repeat = 1;
   private float scale = 1.0f;
   private int delay = 0;
   private float animationSpeed = 3f;
   private float speed = 3f;
   private float rotation = 0f;
   private Path<Animation> path;
   private Integer repeatPath;
   private boolean finishOnPathEnd;
   private boolean finishOnPathFail;
   private float offsetX = 0;
   private float offsetY = 0;

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

   public SimpleAnimationRunner animationSpeed(float speed) {
      this.animationSpeed = speed;
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

   public SimpleAnimationRunner offset(float x, float y) {
      this.offsetX = x;
      this.offsetY = y;
      return this;
   }

   public SimpleAnimationRunner repeatPath(int n) {
      this.repeatPath = n;
      return this;
   }

   public SimpleAnimationRunner path(Path<Animation> path, boolean finishOnEnd, boolean finishOnFail) {
      this.path = path;
      this.finishOnPathEnd = finishOnEnd;
      this.finishOnPathFail = finishOnFail;
      return this;
   }

   @Override
   public CompletableFuture<Void> run(Context context, Layer layer, Vector2fc origin) {
      var animation = new DelayedAnimation(context.createAnimation(animationUid), delay);

      animation.setPosition(origin);
      animation.setPositionOffset(offsetX, offsetY);
      animation.setScale(scale);
      animation.setAnimationSpeed(animationSpeed);
      animation.setSpeed(speed);
      animation.setRotation(rotation);
      animation.setRepeat(repeat);

      if (path != null) {
         animation.followPath(path, repeatPath, finishOnPathEnd, finishOnPathFail);
      }

      layer.pushAnimation(animation);
      return animation.getFuture().thenApply(a -> null);
   }

   @Override
   public CompletableFuture<Void> run(Context context, Layer layer, Movable origin) {
      var animation = new DelayedAnimation(context.createAnimation(animationUid), delay);

      animation.setCoordinates(origin.getCoordinates());
      animation.setPositionOffset(offsetX, offsetY);
      animation.setScale(scale);
      animation.setAnimationSpeed(animationSpeed);
      animation.setSpeed(speed);
      animation.setRotation(rotation);
      animation.setRepeat(repeat);

      if (path != null) {
         animation.followPath(path, repeatPath, finishOnPathEnd, finishOnPathFail);
      }

      layer.pushAnimation(animation);

      return animation.getFuture().thenApply(a -> null);
   }
}
