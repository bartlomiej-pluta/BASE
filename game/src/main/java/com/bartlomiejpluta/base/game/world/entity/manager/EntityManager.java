package com.bartlomiejpluta.base.game.world.entity.manager;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.game.world.entity.model.Entity;
import org.joml.Vector2f;

public interface EntityManager {
   Entity createEntity(Material material, Vector2f coordinateStepSize);
}
