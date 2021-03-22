package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Movable;

public interface PathSegment<T extends Movable> {
   PathProgress perform(T movable, ObjectLayer layer, float dt);
}
