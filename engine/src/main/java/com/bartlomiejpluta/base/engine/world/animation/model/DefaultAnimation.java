package com.bartlomiejpluta.base.engine.world.animation.model;

import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.api.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.AnimationMovement;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.move.Movement;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.world.movement.MovableSprite;
import com.bartlomiejpluta.base.util.path.Path;
import com.bartlomiejpluta.base.util.path.PathExecutor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector2fc;

import java.util.concurrent.CompletableFuture;

import static com.bartlomiejpluta.base.util.path.PathProgress.DONE;
import static com.bartlomiejpluta.base.util.path.PathProgress.SEGMENT_FAILED;

public class DefaultAnimation extends MovableSprite implements Animation {
   private final Vector2fc[] frames;
   private final int lastFrameIndex;
   private final Vector2f animationScale = new Vector2f(1, 1);
   private final Vector2fc animationSpriteSize;

   private int iteration = 0;
   private boolean updated = false;

   @Getter
   @Setter
   private Integer repeat = 1;

   private boolean forcedFinish = false;

   private boolean enabled = true;

   @Getter
   private PathExecutor<Animation> pathExecutor = new PathExecutor<>(this);
   private boolean finishOnEnd;
   private boolean finishOnFail;

   @Getter
   private Layer layer;
   private boolean isObjectLayer = false;

   @Getter
   private final CompletableFuture<Animation> future = new CompletableFuture<>();

   public DefaultAnimation(Mesh mesh, Material material, @NonNull Vector2fc[] frames) {
      super(mesh, material);
      this.frames = frames;
      this.lastFrameIndex = frames.length - 1;

      this.animationSpriteSize = material.getTexture().getSpriteSize();
      super.setScale(animationSpriteSize.x() * animationScale.x, animationSpriteSize.y() * animationScale.y);
   }

   @Override
   public boolean isAnimationEnabled() {
      return enabled;
   }

   @Override
   public void setAnimationEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   @Override
   public void enableAnimation() {
      enabled = true;
   }

   @Override
   public void disableAnimation() {
      enabled = false;
   }

   @Override
   public void toggleAnimationEnabled() {
      enabled = !enabled;
   }

   @Override
   protected boolean shouldAnimate() {
      return enabled;
   }

   @Override
   protected Vector2fc[] getSpriteAnimationFramesPositions() {
      return frames;
   }

   @Override
   protected void setDefaultAnimationFrame() {
      // do nothing
   }

   @Override
   public void followPath(Path<Animation> path, Integer repeat, boolean finishOnEnd, boolean finishOnFail) {
      pathExecutor.setPath(path).setRepeat(repeat);
      this.finishOnEnd = finishOnEnd;
      this.finishOnFail = finishOnFail;
   }

   @Override
   public void update(float dt) {
      super.update(dt);

      if (isObjectLayer) {
         var pathProgress = pathExecutor.execute((ObjectLayer) layer, dt);
         if ((pathProgress == DONE && finishOnEnd) || (pathProgress == SEGMENT_FAILED && finishOnFail)) {
            finish();
         }
      }
   }

   @Override
   public Movement prepareMovement(Direction direction) {
      return new AnimationMovement(this, direction);
   }

   @Override
   public void onAdd(Layer layer) {
      this.layer = layer;
      this.isObjectLayer = layer instanceof ObjectLayer;
   }

   @Override
   public void onFinish(Layer layer) {
      // do nothing
   }

   @Override
   public void finish() {
      this.forcedFinish = true;
   }

   @Override
   public boolean finished() {
      if (forcedFinish) {
         return true;
      }

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

   @Override
   public void setScaleX(float scaleX) {
      this.animationScale.x = scaleX;
      super.setScaleX(animationSpriteSize.x() * scaleX);
   }

   @Override
   public void setScaleY(float scaleY) {
      this.animationScale.y = scaleY;
      super.setScaleY(animationSpriteSize.y() * scaleY);
   }

   @Override
   public void setScale(float scale) {
      this.animationScale.x = scale;
      this.animationScale.y = scale;
      super.setScale(animationSpriteSize.x() * scale, animationSpriteSize.y() * scale);
   }

   @Override
   public void setScale(float scaleX, float scaleY) {
      this.animationScale.x = scaleX;
      this.animationScale.y = scaleY;
      super.setScale(animationSpriteSize.x() * scaleX, animationSpriteSize.y() * scaleY);
   }

   @Override
   public float getScaleX() {
      return animationScale.x;
   }

   @Override
   public float getScaleY() {
      return animationScale.y;
   }
}
