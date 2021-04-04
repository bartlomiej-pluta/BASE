package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.move.Movable;

import java.util.List;

public interface Path<T extends Movable> {
   List<? extends PathSegment<T>> getPath();
}
