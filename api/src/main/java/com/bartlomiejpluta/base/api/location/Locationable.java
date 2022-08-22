package com.bartlomiejpluta.base.api.location;

import com.bartlomiejpluta.base.internal.object.Placeable;
import org.joml.Vector2fc;
import org.joml.Vector2ic;

public interface Locationable extends Placeable {
   void setStepSize(float x, float y);

   Vector2ic getCoordinates();

   void setCoordinates(Vector2ic coordinates);

   void setCoordinates(int x, int y);

   Vector2fc getPositionOffset();

   void setPositionOffset(Vector2fc offset);

   void setPositionOffset(float offsetX, float offsetY);

   int chebyshevDistance(Vector2ic coordinates);

   int manhattanDistance(Vector2ic coordinates);
}
