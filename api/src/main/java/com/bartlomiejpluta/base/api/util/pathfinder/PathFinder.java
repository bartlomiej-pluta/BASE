package com.bartlomiejpluta.base.api.util.pathfinder;

import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import org.joml.Vector2ic;

import java.util.LinkedList;

public interface PathFinder {
   LinkedList<Vector2ic> findPath(ObjectLayer layer, Vector2ic start, Vector2ic end);
}
