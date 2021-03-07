package com.bartlomiejpluta.base.api.game.map.layer.base;

import com.bartlomiejpluta.base.api.game.map.model.GameMap;
import com.bartlomiejpluta.base.api.internal.logic.Updatable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;

public interface Layer extends Renderable, Updatable {
   GameMap getMap();
}
