package com.bartlomiejpluta.base.lib.animation;

import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.util.path.Path;
import org.joml.Vector2fc;
import org.joml.Vector2i;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class BulletAnimationRunner implements AnimationRunner {
   private static final Path<Animation> UP = new Path<Animation>().move(Direction.UP);
   private static final Path<Animation> DOWN = new Path<Animation>().move(Direction.DOWN);
   private static final Path<Animation> LEFT = new Path<Animation>().move(Direction.LEFT);
   private static final Path<Animation> RIGHT = new Path<Animation>().move(Direction.RIGHT);

   private final String animationUid;

   private Integer range;
   private Integer repeat = 1;
   private float scale = 1.0f;
   private int delay = 0;
   private float animationSpeed = 0.05f;
   private float speed = 0.05f;
   private float rotation = 0f;
   private Direction direction;
   private Path<Animation> path;
   private Consumer<Entity> onHit;
   private float offsetX = 0;
   private float offsetY = 0;

   public BulletAnimationRunner(String animationUid) {
      this.animationUid = animationUid;
   }

   public BulletAnimationRunner onHit(Consumer<Entity> runnable) {
      this.onHit = requireNonNull(runnable);
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

   public BulletAnimationRunner direction(Direction direction) {
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
   public void run(Context context, Layer layer, Vector2fc origin) {
      var animation = new BulletAnimation(context.createAnimation(animationUid), delay, direction, onHit);
      animation.setPosition(origin.x() + offsetX, origin.y() + offsetY);
      animation.setScale(scale);
      animation.setAnimationSpeed(animationSpeed);
      animation.setSpeed(speed);
      animation.setRotation(rotation);
      animation.setRepeat(repeat);
      animation.setPositionOffset(offsetX, offsetY);

      animation.followPath(path, range, true, true);

      layer.pushAnimation(animation);
   }

   private static class BulletAnimation extends DelayedAnimation {
      private final Direction direction;
      private final Consumer<Entity> action;

      public BulletAnimation(Animation animation, int delay, Direction direction, Consumer<Entity> action) {
         super(animation, delay);
         this.direction = direction;
         this.action = action;
      }

      @Override
      public void onFinish(Layer layer) {
         if (action != null) {
            var target = getCoordinates().add(direction.vector, new Vector2i());
            if (layer instanceof ObjectLayer) {
               for (var entity : ((ObjectLayer) layer).getEntities()) {
                  var movement = entity.getMovement();
                  if (entity.getCoordinates().equals(target) || movement != null && movement.getTo().equals(target)) {
                     action.accept(entity);
                     return;
                  }
               }
            }
         }
      }
   }
}
