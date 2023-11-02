package com.bartlomiejpluta.base.lib.animation;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.move.Movable;
import lombok.NonNull;
import org.joml.Vector2fc;

import java.util.concurrent.CompletableFuture;

public interface AnimationRunner {
   CompletableFuture<Void> run(Context context, Layer layer, Vector2fc origin);

   CompletableFuture<Void> run(Context context, Layer layer, Movable origin);

   default CompletableFuture<Void> run(Context context, Entity entity) {
      return run(context, entity.getLayer(), entity.getPosition());
   }

   static SimpleAnimationRunner simple(@NonNull String animationUid) {
      return new SimpleAnimationRunner(animationUid);
   }

   static RandomAnimationsRunner random(int count) {
      return new RandomAnimationsRunner(count);
   }

   static BulletAnimationRunner bullet(@NonNull String animationUid) {
      return new BulletAnimationRunner(animationUid);
   }
}
