package com.bartlomiejpluta.base.api.game.ai.pathfinding;

import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import org.joml.Vector2i;

import java.util.List;

public interface PathFinder {
   List<Vector2i> findPath(ObjectLayer layer, Vector2i start, Vector2i end, int range);
}
