package com.bartlomiejpluta.base.game.world.entity.manager;

import com.bartlomiejpluta.base.core.gc.Cleanable;
import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.game.map.model.GameMap;
import com.bartlomiejpluta.base.game.world.entity.model.Entity;

public interface EntityManager extends Cleanable {
   Entity createEntity(Material material, GameMap map);
}
