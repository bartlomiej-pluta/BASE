package com.bartlomiejpluta.base.core.world.animation;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.world.object.RenderableObject;
import org.joml.Vector2f;
import org.joml.Vector2i;

public abstract class AnimationableObject extends RenderableObject {
   public AnimationableObject(Mesh mesh, Material material, Vector2i spriteSheetDimension) {
      super(mesh);
      setMaterial(material);
      material.setSpriteSize(1 / (float) spriteSheetDimension.x, 1 / (float) spriteSheetDimension.y);
   }

   // Returns time in ms between frames
   public abstract int getAnimationSpeed();

   public abstract boolean shouldAnimate();

   public abstract Vector2f[] getSpriteAnimationFramesPositions();

   protected void setAnimationFrame(Vector2f framePosition) {
      var material = this.getMaterial();
      var spriteSize = material.getSpriteSize();
      material.setSpritePosition(spriteSize.x * framePosition.x, spriteSize.y * framePosition.y);
   }
}
