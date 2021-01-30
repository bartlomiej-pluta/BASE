package com.bartlomiejpluta.samplegame.game.object;

import com.bartlomiejpluta.samplegame.core.gl.object.material.Material;
import com.bartlomiejpluta.samplegame.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.samplegame.core.world.object.RenderableObject;

public class Sprite extends RenderableObject {
   private static final float[] VERTICES = new float[]{
           -100, 100,
           -100, -100,
           100, -100,
           100, 100
   };

   private static final float[] TEX_COORDS = new float[] {
           0, 0,
           0, 1,
           1, 1,
           1, 0
   };

   private static final int[] ELEMENTS = new int[]{
           0, 1, 2,
           2, 3, 0
   };

   public Sprite(Material material) {
      super(new Mesh(VERTICES, TEX_COORDS, ELEMENTS));
      setMaterial(material);
   }
}