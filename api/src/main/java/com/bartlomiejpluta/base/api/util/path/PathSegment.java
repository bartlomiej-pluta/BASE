package com.bartlomiejpluta.base.api.util.path;

import com.bartlomiejpluta.base.api.game.entity.Movable;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;

public interface PathSegment<T extends Movable> {
   boolean perform(T movable, ObjectLayer layer, float dt);
}
