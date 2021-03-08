package com.bartlomiejpluta.base.api.util.pathfinding;

import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import org.joml.Vector2i;

import java.util.LinkedList;

public interface PathFinder {
   LinkedList<Vector2i> findPath(ObjectLayer layer, Vector2i start, Vector2i end, int range);
}
