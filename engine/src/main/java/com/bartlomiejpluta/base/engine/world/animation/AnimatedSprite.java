package com.bartlomiejpluta.base.engine.world.animation;

import com.bartlomiejpluta.base.api.internal.camera.Camera;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.api.internal.window.Window;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.world.object.Sprite;
import lombok.EqualsAndHashCode;
import org.joml.Vector2f;

@EqualsAndHashCode(callSuper = true)
public abstract class AnimatedSprite extends Sprite {

   public AnimatedSprite(Mesh mesh, Material material) {
      super(mesh, material);
   }

   // Returns time in ms between frames
   public abstract int getAnimationSpeed();

   public abstract boolean shouldAnimate();

   public abstract Vector2f[] getSpriteAnimationFramesPositions();

   @Override
   public void render(Window window, Camera camera, ShaderManager shaderManager) {
      animate();
      super.render(window, camera, shaderManager);
   }

   private void animate() {
      if (shouldAnimate()) {
         var positions = getSpriteAnimationFramesPositions();
         var delay = getAnimationSpeed();
         var currentPosition = (int) (System.currentTimeMillis() % (positions.length * delay)) / delay;
         var current = positions[currentPosition];
         material.setSpritePosition(current);
      }
   }
}
