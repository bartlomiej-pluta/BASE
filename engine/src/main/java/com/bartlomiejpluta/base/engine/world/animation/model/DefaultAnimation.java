package com.bartlomiejpluta.base.engine.world.animation.model;

import com.bartlomiejpluta.base.api.game.animation.Animation;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.util.math.MathUtil;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.world.movement.MovableSprite;
import lombok.NonNull;
import org.joml.Vector2fc;

public class DefaultAnimation extends MovableSprite implements Animation {
   private final Vector2fc[] frames;
   private int animationSpeed = 100;

   public DefaultAnimation(Mesh mesh, Material material, @NonNull Vector2fc[] frames) {
      super(mesh, material);
      this.frames = frames;
   }

   @Override
   public void setSpeed(float speed) {
      framesToCrossOneTile = (int) (1 / MathUtil.clamp(speed, Float.MIN_VALUE, 1.0));
   }

   @Override
   public void setAnimationSpeed(float speed) {
      animationSpeed = (int) (1 / MathUtil.clamp(speed, Float.MIN_VALUE, 1.0));
   }

   @Override
   public void onAdd(ObjectLayer layer) {
      // do nothing
   }

   @Override
   public void onRemove(ObjectLayer layer) {
      // do nothing
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
}
