package com.bartlomiejpluta.base.api.util.path;

import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.game.move.Movable;

public interface PathSegment<T extends Movable> {
   PathProgress perform(T movable, ObjectLayer layer, float dt);
}
