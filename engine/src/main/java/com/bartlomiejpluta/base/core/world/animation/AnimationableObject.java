package com.bartlomiejpluta.base.core.world.animation;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.world.object.RenderableObject;
import org.joml.Vector2f;
import org.joml.Vector2i;

public abstract class AnimationableObject extends RenderableObject {
   public AnimationableObject(Mesh mesh, Material material) {
      super(mesh);
      setMaterial(material);
      var dimensions = getSpriteSheetDimensions();
      material.setSpriteSize(1/(float) dimensions.x, 1/(float) dimensions.y);
   }

   // Returns time in ms between frames
   public abstract int getAnimationSpeed();

   public abstract Vector2i getSpriteSheetDimensions();

   public abstract boolean shouldAnimate();

   public abstract Vector2f[] getSpriteAnimationFramesPositions();
}
