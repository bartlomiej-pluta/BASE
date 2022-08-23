package com.bartlomiejpluta.base.engine.world.location;

import com.bartlomiejpluta.base.api.location.Locationable;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.engine.world.object.Model;
import com.bartlomiejpluta.base.util.math.Distance;
import lombok.EqualsAndHashCode;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;
import org.joml.Vector2ic;

@EqualsAndHashCode(callSuper = true)
public abstract class LocationableModel extends Model implements Locationable {
   protected final Vector2f coordinateStepSize = new Vector2f(0, 0);
   protected final Vector2i coordinates = new Vector2i(0, 0);
   protected final Vector2f positionOffset = new Vector2f(0, 0);
   private PlacingMode placingMode;

   public LocationableModel() {
      setCoordinates(0, 0);
   }

   @Override
   public Vector2ic getCoordinates() {
      return coordinates;
   }

   @Override
   public void setCoordinates(Vector2ic coordinates) {
      setCoordinates(coordinates.x(), coordinates.y());
   }

   @Override
   public void setCoordinates(int x, int y) {
      coordinates.x = x;
      coordinates.y = y;
      super.setPosition((x + 0.5f) * coordinateStepSize.x + positionOffset.x, (y + 0.5f) * coordinateStepSize.y + positionOffset.y);
      placingMode = PlacingMode.BY_COORDINATES;
   }

   @Override
   public void setPosition(float x, float y) {
      super.setPosition(x + positionOffset.x, y + positionOffset.y);
      coordinates.x = (int) (x / coordinateStepSize.x);
      coordinates.y = (int) (y / coordinateStepSize.y);
      placingMode = PlacingMode.BY_POSITION;
   }


   @Override
   public void setPosition(Vector2fc position) {
      setPosition(position.x(), position.y());
   }

   public void setStepSize(float x, float y) {
      coordinateStepSize.x = x;
      coordinateStepSize.y = y;
      adjustPosition();
   }

   private void adjustPosition() {
      switch (placingMode) {
         case BY_POSITION -> setPosition(position);
         case BY_COORDINATES -> setCoordinates(coordinates);
      }
   }

   @Override
   public Vector2fc getPositionOffset() {
      return positionOffset;
   }

   @Override
   public void setPositionOffset(Vector2fc offset) {
      setPositionOffset(offset.x(), offset.y());
   }

   @Override
   public void setPositionOffset(float offsetX, float offsetY) {
      this.positionOffset.x = offsetX;
      this.positionOffset.y = offsetY;
      adjustPosition();
   }

   @Override
   public Direction getDirectionTowards(Locationable target) {
      return Direction.ofVector(target.getCoordinates().sub(getCoordinates(), new Vector2i()));
   }

   @Override
   public int chebyshevDistance(Vector2ic coordinates) {
      return Distance.chebyshev(this.coordinates, coordinates);
   }

   @Override
   public int manhattanDistance(Vector2ic coordinates) {
      return Distance.manhattan(this.coordinates, coordinates);
   }

   @Override
   public int chebyshevDistance(Locationable other) {
      return Distance.chebyshev(this.coordinates, other.getCoordinates());
   }

   @Override
   public int manhattanDistance(Locationable other) {
      return Distance.manhattan(this.coordinates, other.getCoordinates());
   }

   private enum PlacingMode {BY_POSITION, BY_COORDINATES}
}
