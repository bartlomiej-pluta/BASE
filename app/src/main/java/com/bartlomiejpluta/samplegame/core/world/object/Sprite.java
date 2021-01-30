package com.bartlomiejpluta.samplegame.core.world.object;

import com.bartlomiejpluta.samplegame.core.gl.object.mesh.Mesh;

public class Sprite extends Object {
   private static final float[] VERTICES = new float[]{
           -0.5f,  0.5f, 0.0f,
           -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f,  0.5f, 0.0f
   };
   
   private static final int[] ELEMENTS = new int[]{
           0, 1, 2,
           2, 3, 0
   };

   public Sprite() {
      super(new Mesh(VERTICES, ELEMENTS));
   }
}
