package com.bartlomiejpluta.base.game.entity.manager;

import com.bartlomiejpluta.base.core.gc.Cleanable;
import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.game.entity.model.Entity;
import com.bartlomiejpluta.base.game.map.model.GameMap;

public interface EntityManager extends Cleanable {
   Entity createEntity(Material material, GameMap map);
}
