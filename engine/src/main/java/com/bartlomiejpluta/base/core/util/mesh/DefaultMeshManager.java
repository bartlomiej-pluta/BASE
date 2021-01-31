package com.bartlomiejpluta.base.core.util.mesh;

import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import lombok.Data;
import org.joml.Vector2f;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DefaultMeshManager implements MeshManager {
   private final Map<QuadDimension, Mesh> quads = new HashMap<>();

   @Override
   public Mesh createQuad(float width, float height, float originX, float originY) {
      var dim = new QuadDimension(new Vector2f(width, height), new Vector2f(originX, originY));
      var mesh = quads.get(dim);

      if(mesh == null) {
         mesh = Mesh.quad(width, height, originX, originY);
         quads.put(dim, mesh);
      }

      return mesh;
   }

   @Data
   private static class QuadDimension {
      private final Vector2f size;
      private final Vector2f origin;
   }
}
