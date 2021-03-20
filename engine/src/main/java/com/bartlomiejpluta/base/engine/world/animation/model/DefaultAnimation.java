package com.bartlomiejpluta.base.engine.world.animation.model;

import com.bartlomiejpluta.base.api.game.animation.Animation;
import com.bartlomiejpluta.base.api.game.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.util.math.MathUtil;
import com.bartlomiejpluta.base.api.util.path.Path;
import com.bartlomiejpluta.base.api.util.path.PathExecutor;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.world.movement.MovableSprite;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.joml.Vector2fc;

public class DefaultAnimation extends MovableSprite implements Animation {
   private final Vector2fc[] frames;
   private final int lastFrameIndex;

   private int animationSpeed = 100;
   private int iteration = 0;
   private boolean updated = false;

   @Getter
   @Setter
   private Integer repeat = 1;

   @Getter
   private PathExecutor<Animation> pathExecutor;
   private Layer layer;
   private boolean isObjectLayer = false;

   public DefaultAnimation(Mesh mesh, Material material, @NonNull Vector2fc[] frames) {
      super(mesh, material);
      this.frames = frames;
      this.lastFrameIndex = frames.length - 1;
   }

   @Override
   public void setAnimationSpeed(float speed) {
      animationSpeed = (int) (1 / MathUtil.clamp(speed, Float.MIN_VALUE, 1.0));
   }

   @Override
   public int getAnimationSpeed() {
      return animationSpeed;
   }

   @Override
   public boolean shouldAnimate() {
      return true;
   }

   @Override
   public Vector2fc[] getSpriteAnimationFramesPositions() {
      return frames;
   }

   @Override
   protected void setDefaultAnimationFrame() {
      // do nothing
   }

   @Override
   public void followPath(Path<Animation> path, boolean repeat) {
      pathExecutor = new PathExecutor<>(this, repeat, path);
   }

   @Override
   public void setSpeed(float speed) {
      framesToCrossOneTile = (int) (1 / MathUtil.clamp(speed, Float.MIN_VALUE, 1.0));
   }

   @Override
   public void update(float dt) {
      super.update(dt);

      if (pathExecutor != null && isObjectLayer) {
         pathExecutor.execute((ObjectLayer) layer, dt);
      }
   }

   @Override
   public void onAdd(Layer layer) {
      this.layer = layer;
      this.isObjectLayer = layer instanceof ObjectLayer;
   }

   @Override
   public boolean finished() {
      if (repeat == null) {
         return false;
      }

      if (currentAnimationFrame == 0) {
         updated = false;
      }

      if (currentAnimationFrame == lastFrameIndex && !updated) {
         ++iteration;
         updated = true;
      }

      return iteration >= repeat;
   }
}
