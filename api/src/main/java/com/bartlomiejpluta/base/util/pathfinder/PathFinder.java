package com.bartlomiejpluta.base.util.pathfinder;

import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Movable;
import com.bartlomiejpluta.base.util.path.MovementPath;
import org.joml.Vector2ic;

import java.util.LinkedList;

public interface PathFinder {
   <T extends Movable> MovementPath<T> findPath(ObjectLayer layer, T start, Vector2ic end);

   LinkedList<Vector2ic> findSequence(ObjectLayer layer, Vector2ic start, Vector2ic end);
}
