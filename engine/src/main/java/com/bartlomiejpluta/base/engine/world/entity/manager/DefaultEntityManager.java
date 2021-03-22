package com.bartlomiejpluta.base.engine.world.entity.manager;

import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.util.mesh.MeshManager;
import com.bartlomiejpluta.base.engine.world.entity.config.EntitySpriteConfiguration;
import com.bartlomiejpluta.base.engine.world.entity.model.DefaultEntity;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2ic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Map.Entry;

import static java.util.stream.Collectors.toUnmodifiableMap;

@Slf4j
@Component
public class DefaultEntityManager implements EntityManager {
   private final MeshManager meshManager;
   private final EntitySetManager entitySetManager;

   private final Map<Direction, Integer> spriteDirectionRows;
   private final Map<Direction, Vector2fc> spriteDefaultRows;
   private final Vector2ic entitySpriteDimension;

   @Autowired
   public DefaultEntityManager(MeshManager meshManager, EntitySetManager entitySetManager, EntitySpriteConfiguration configuration) {
      this.meshManager = meshManager;
      this.entitySetManager = entitySetManager;

      this.entitySpriteDimension = configuration.getDimension().asVector();
      this.spriteDirectionRows = configuration.getSpriteDirectionRows();

      var defaultSpriteColumn = configuration.getDefaultSpriteColumn();
      this.spriteDefaultRows = spriteDirectionRows
            .entrySet()
            .stream()
            .collect(toUnmodifiableMap(Entry::getKey, entry -> new Vector2f(defaultSpriteColumn, entry.getValue())));
   }

   @Override
   public Entity createEntity(String entitySetUid) {
      var material = entitySetManager.loadObject(entitySetUid);
      return new DefaultEntity(buildMesh(material), material, spriteDirectionRows, spriteDefaultRows);
   }

   private Mesh buildMesh(Material material) {
      var texture = material.getTexture();
      var spriteWidth = texture.getWidth() / (float) entitySpriteDimension.x();
      var spriteHeight = texture.getHeight() / (float) entitySpriteDimension.y();
      return meshManager.createQuad(spriteWidth, spriteHeight, spriteWidth / 2, spriteHeight * 0.9f);
   }

   @Override
   public void cleanUp() {
      log.info("There is nothing to clean up here");
   }
}
