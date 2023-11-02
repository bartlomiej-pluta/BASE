package com.bartlomiejpluta.base.lib.animation;

import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.move.Movable;
import com.bartlomiejpluta.base.util.path.BasePath;
import com.bartlomiejpluta.base.util.path.Path;
import lombok.NonNull;
import org.joml.Vector2fc;
import org.joml.Vector2i;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BulletAnimationRunner implements AnimationRunner {
   private static final Path<Animation> UP = new BasePath<Animation>().move(Direction.UP);
   private static final Path<Animation> DOWN = new BasePath<Animation>().move(Direction.DOWN);
   private static final Path<Animation> LEFT = new BasePath<Animation>().move(Direction.LEFT);
   private static final Path<Animation> RIGHT = new BasePath<Animation>().move(Direction.RIGHT);

   private final String animationUid;

   private Integer range;
   private Integer repeat = 1;
   private float scale = 1.0f;
   private int delay = 0;
   private float animationSpeed = 3f;
   private float speed = 3f;
   private float rotation = 0f;
   private Direction direction;
   private Path<Animation> path;
   private BiConsumer<Movable, Entity> onHit;
   private BiConsumer<Movable, Animation> onMiss;
   private float offsetX = 0;
   private float offsetY = 0;

   public BulletAnimationRunner(@NonNull String animationUid) {
      this.animationUid = animationUid;
   }

   public BulletAnimationRunner onHit(@NonNull BiConsumer<Movable, Entity> action) {
      this.onHit = action;
      return this;
   }

   public BulletAnimationRunner onHit(@NonNull Consumer<Entity> action) {
      this.onHit = (m, e) -> action.accept(e);
      return this;
   }

   public BulletAnimationRunner onMiss(@NonNull BiConsumer<Movable, Animation> action) {
      this.onMiss = action;
      return this;
   }

   public BulletAnimationRunner onMiss(@NonNull Consumer<Animation> action) {
      this.onMiss = (m, a) -> action.accept(a);
      return this;
   }

   public BulletAnimationRunner range(int n) {
      this.range = n;
      return this;
   }

   public BulletAnimationRunner infiniteRange() {
      this.range = null;
      return this;
   }

   public BulletAnimationRunner repeat(int n) {
      this.repeat = n;
      return this;
   }

   public BulletAnimationRunner infinite() {
      this.repeat = null;
      return this;
   }

   public BulletAnimationRunner scale(float scale) {
      this.scale = scale;
      return this;
   }

   public BulletAnimationRunner delay(int delay) {
      this.delay = delay;
      return this;
   }

   public BulletAnimationRunner animationSpeed(float speed) {
      this.animationSpeed = speed;
      return this;
   }

   public BulletAnimationRunner speed(float speed) {
      this.speed = speed;
      return this;
   }

   public BulletAnimationRunner rotation(float rotation) {
      this.rotation = rotation;
      return this;
   }

   public BulletAnimationRunner offset(float x, float y) {
      this.offsetX = x;
      this.offsetY = y;
      return this;
   }

   public BulletAnimationRunner direction(@NonNull Direction direction) {
      this.direction = direction;
      this.path = switch (direction) {
         case UP -> UP;
         case DOWN -> DOWN;
         case LEFT -> LEFT;
         case RIGHT -> RIGHT;
      };

      return this;
   }

   @Override
   public CompletableFuture<Void> run(Context context, Layer layer, Vector2fc origin) {
      var animation = new BulletAnimation(context.createAnimation(animationUid), delay, direction, null, onHit, onMiss);

      animation.setPosition(origin);
      animation.setScale(scale);
      animation.setAnimationSpeed(animationSpeed);
      animation.setSpeed(speed);
      animation.setRotation(rotation);
      animation.setRepeat(repeat);
      animation.setPositionOffset(offsetX, offsetY);

      animation.followPath(path, range, true, true);

      layer.pushAnimation(animation);

      return animation.getFuture().thenApply(a -> null);
   }

   @Override
   public CompletableFuture<Void> run(Context context, Layer layer, Movable origin) {
      var animation = new BulletAnimation(context.createAnimation(animationUid), delay, direction, origin, onHit, onMiss);

      animation.setCoordinates(origin.getCoordinates());
      animation.setScale(scale);
      animation.setAnimationSpeed(animationSpeed);
      animation.setSpeed(speed);
      animation.setRotation(rotation);
      animation.setRepeat(repeat);
      animation.setPositionOffset(offsetX, offsetY);

      animation.followPath(path, range, true, true);

      layer.pushAnimation(animation);

      return animation.getFuture().thenApply(a -> null);
   }

   private static class BulletAnimation extends DelayedAnimation {
      private final Direction direction;
      private final Movable movable;
      private final BiConsumer<Movable, Entity> onHit;
      private final BiConsumer<Movable, Animation> onMiss;

      public BulletAnimation(Animation animation, int delay, Direction direction, Movable movable, BiConsumer<Movable, Entity> onHit, BiConsumer<Movable, Animation> onMiss) {
         super(animation, delay);
         this.direction = direction;
         this.movable = movable;
         this.onHit = onHit;
         this.onMiss = onMiss;
      }

      @Override
      public void onFinish(Layer layer) {
         if (onHit != null) {
            var target = getCoordinates().add(direction.vector, new Vector2i());
            if (layer instanceof ObjectLayer) {
               for (var entity : ((ObjectLayer) layer).getEntities()) {
                  var movement = (entity instanceof Movable) ? ((Movable) entity).getMovement() : null;
                  if ((entity.getCoordinates().equals(target) || movement != null && movement.getTo().equals(target)) && entity.isBlocking()) {
                     onHit.accept(movable, entity);
                     return;
                  }
               }
            }
         }

         if (onMiss != null) {
            onMiss.accept(movable, this);
         }
      }
   }
}
