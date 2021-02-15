package com.bartlomiejpluta.base.core.world.animation;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.world.object.RenderableObject;
import lombok.EqualsAndHashCode;
import org.joml.Vector2f;
import org.joml.Vector2i;

@EqualsAndHashCode(callSuper = true)
public abstract class AnimationableObject extends RenderableObject {
   protected final Vector2i spriteSheetDimension;

   public AnimationableObject(Mesh mesh, Material material, Vector2i spriteSheetDimension) {
      super(mesh);
      this.spriteSheetDimension = spriteSheetDimension;

      material.setSpriteSize(1 / (float) spriteSheetDimension.y, 1 / (float) spriteSheetDimension.x);
      setMaterial(material);
   }

   // Returns time in ms between frames
   public abstract int getAnimationSpeed();

   public abstract boolean shouldAnimate();

   public abstract Vector2f[] getSpriteAnimationFramesPositions();

   protected void setAnimationFrame(Vector2f framePosition) {
      var spriteSize = getMaterial().getSpriteSize();
      setSpritePosition(spriteSize.x * framePosition.x, spriteSize.y * framePosition.y);
   }

   @Override
   public void render(Window window, ShaderManager shaderManager) {
      animate();
      super.render(window, shaderManager);
   }

   private void animate() {
      if (shouldAnimate()) {
         var positions = getSpriteAnimationFramesPositions();
         var delay = getAnimationSpeed();
         var currentPosition = (int) (System.currentTimeMillis() % (positions.length * delay)) / delay;
         var current = positions[currentPosition];
         setAnimationFrame(current);
      }
   }
}
