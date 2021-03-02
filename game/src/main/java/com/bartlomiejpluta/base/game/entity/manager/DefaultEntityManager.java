package com.bartlomiejpluta.base.game.entity.manager;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.util.mesh.MeshManager;
import com.bartlomiejpluta.base.game.entity.config.EntitySpriteConfiguration;
import com.bartlomiejpluta.base.game.entity.model.DefaultEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultEntityManager implements EntityManager {
   private final MeshManager meshManager;
   private final EntitySetManager entitySetManager;
   private final EntitySpriteConfiguration configuration;

   @Override
   public DefaultEntity createEntity(String entitySetUid) {
      var material = entitySetManager.loadObject(entitySetUid);
      return new DefaultEntity(buildMesh(material), material, configuration);
   }

   private Mesh buildMesh(Material material) {
      var texture = material.getTexture();
      var dimension = configuration.getDimension().asVector();
      var spriteWidth = texture.getWidth() / (float) dimension.y;
      var spriteHeight = texture.getHeight() / (float) dimension.x;
      return meshManager.createQuad(spriteWidth, spriteHeight, spriteWidth / 2, spriteHeight*0.9f);
   }

   @Override
   public void cleanUp() {
      log.info("There is nothing to clean up here");
   }
}
