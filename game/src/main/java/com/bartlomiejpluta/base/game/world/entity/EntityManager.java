package com.bartlomiejpluta.base.game.world.entity;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import org.joml.Vector2f;

public interface EntityManager {
   Entity createEntity(Material material, Vector2f coordinateStepSize);
}
