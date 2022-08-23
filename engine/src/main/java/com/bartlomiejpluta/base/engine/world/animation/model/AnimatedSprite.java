package com.bartlomiejpluta.base.engine.world.animation.model;

import com.bartlomiejpluta.base.api.animation.Animated;
import com.bartlomiejpluta.base.engine.core.engine.DefaultGameEngine;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.world.object.Sprite;
import com.bartlomiejpluta.base.util.math.MathUtil;
import lombok.EqualsAndHashCode;
import org.joml.Vector2fc;

@EqualsAndHashCode(callSuper = true)
public abstract class AnimatedSprite extends Sprite implements Animated {
   private int time;

   // The time in ms between frames
   private int intervalInMilliseconds = 100;
   protected int currentAnimationFrame;

   public AnimatedSprite(Mesh mesh, Material material) {
      super(mesh, material);
   }

   protected abstract boolean shouldAnimate();

   protected abstract Vector2fc[] getSpriteAnimationFramesPositions();

   @Override
   public void setAnimationSpeed(float speed) {
      intervalInMilliseconds = (int) (1 / MathUtil.clamp(speed / DefaultGameEngine.TARGET_UPS, Float.MIN_VALUE, 1.0));
   }

   @Override
   public float getAnimationSpeed() {
      return 1 / (float) intervalInMilliseconds;
   }

   @Override
   public void setAnimationFrame(int frame) {
      var positions = getSpriteAnimationFramesPositions();
      currentAnimationFrame = frame % positions.length;
      var current = positions[currentAnimationFrame];
      material.setSpritePosition(current);
   }

   @Override
   public void update(float dt) {
      if (shouldAnimate()) {
         time += dt * 1000;
         var positions = getSpriteAnimationFramesPositions();
         currentAnimationFrame = ((time % (positions.length * intervalInMilliseconds)) / intervalInMilliseconds);
         var current = positions[currentAnimationFrame];
         material.setSpritePosition(current);
      } else {
         time = 0;
      }
   }
}
