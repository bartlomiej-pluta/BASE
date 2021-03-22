package com.bartlomiejpluta.base.engine.world.animation.model;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.logic.Updatable;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.world.object.Sprite;
import lombok.EqualsAndHashCode;
import org.joml.Vector2fc;

@EqualsAndHashCode(callSuper = true)
public abstract class AnimatedSprite extends Sprite implements Updatable {
   private int time;
   protected int currentAnimationFrame;

   public AnimatedSprite(Mesh mesh, Material material) {
      super(mesh, material);
   }

   // Returns time in ms between frames
   public abstract int getAnimationSpeed();

   public abstract boolean shouldAnimate();

   public abstract Vector2fc[] getSpriteAnimationFramesPositions();

   @Override
   public void update(float dt) {
      time += dt * 1000;
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      animate();
      super.render(screen, camera, shaderManager);
   }

   private void animate() {
      if (shouldAnimate()) {
         var positions = getSpriteAnimationFramesPositions();
         var delay = getAnimationSpeed();
         currentAnimationFrame = ((time % (positions.length * delay)) / delay);
         var current = positions[currentAnimationFrame];
         material.setSpritePosition(current);
      }
   }
}