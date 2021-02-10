package com.bartlomiejpluta.base.core.util.mesh;

import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class DefaultMeshManager implements MeshManager {
   private final Map<QuadDimension, Mesh> quads = new HashMap<>();

   @Override
   public Mesh createQuad(float width, float height, float originX, float originY) {
      var dim = new QuadDimension(new Vector2f(width, height), new Vector2f(originX, originY));
      var mesh = quads.get(dim);

      if(mesh == null) {
         log.info("Creating [w:{}, h:{} | O:{},{}] and putting it into the cache", width, height, originX, originY);
         mesh = Mesh.quad(width, height, originX, originY);
         quads.put(dim, mesh);
      }

      return mesh;
   }

   @Override
   public void cleanUp() {
      log.info("Disposing meshes");
      quads.forEach((dim, mesh) -> mesh.dispose());
      log.info("{} meshes has been disposed", quads.size());
   }

   @Data
   private static class QuadDimension {
      private final Vector2f size;
      private final Vector2f origin;
   }
}
