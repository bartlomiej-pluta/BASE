package com.bartlomiejpluta.base.game.animation;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.world.camera.Camera;
import com.bartlomiejpluta.base.core.world.object.Sprite;
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
