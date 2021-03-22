package com.bartlomiejpluta.base.util.pathfinder;

import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import org.joml.Vector2ic;

import java.util.LinkedList;

public interface PathFinder {
   LinkedList<Vector2ic> findPath(ObjectLayer layer, Vector2ic start, Vector2ic end);
}
