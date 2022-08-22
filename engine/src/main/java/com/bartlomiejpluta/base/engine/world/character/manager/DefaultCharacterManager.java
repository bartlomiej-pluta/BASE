package com.bartlomiejpluta.base.engine.world.character.manager;

import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.util.mesh.MeshManager;
import com.bartlomiejpluta.base.engine.world.character.config.CharacterSpriteConfiguration;
import com.bartlomiejpluta.base.engine.world.character.model.DefaultCharacter;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Map.Entry;

import static java.util.stream.Collectors.toUnmodifiableMap;

@Slf4j
@Component
public class DefaultCharacterManager implements CharacterManager {
   private final MeshManager meshManager;
   private final CharacterSetManager characterSetManager;

   private final int defaultSpriteColumn;
   private final Map<Direction, Integer> spriteDirectionRows;
   private final Map<Direction, Vector2fc> spriteDefaultRows;

   private Mesh mesh;

   @Autowired
   public DefaultCharacterManager(MeshManager meshManager, CharacterSetManager characterSetManager, CharacterSpriteConfiguration configuration) {
      this.meshManager = meshManager;
      this.characterSetManager = characterSetManager;

      this.spriteDirectionRows = configuration.getSpriteDirectionRows();

      defaultSpriteColumn = configuration.getDefaultSpriteColumn();
      this.spriteDefaultRows = spriteDirectionRows
              .entrySet()
              .stream()
              .collect(toUnmodifiableMap(Entry::getKey, entry -> new Vector2f(defaultSpriteColumn, entry.getValue())));
   }

   @Override
   public void init() {
      mesh = meshManager.createQuad(1, 1, 0.5f, 1);
   }

   @Override
   public Character createCharacter(String characterSetUid) {
      return new DefaultCharacter(mesh, characterSetManager, defaultSpriteColumn, spriteDirectionRows, spriteDefaultRows, characterSetUid);
   }

   @Override
   public void cleanUp() {
      log.info("There is nothing to clean up here");
   }
}
