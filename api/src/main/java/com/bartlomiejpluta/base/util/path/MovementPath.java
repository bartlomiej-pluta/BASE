package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.move.Movable;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.util.ArrayList;
import java.util.List;

public class MovementPath<T extends Movable> implements Path<T> {
   private final List<PositionableMoveSegment<T>> path = new ArrayList<>();

   @Override
   public int getLength() {
      return path.size();
   }

   @Override
   public boolean isEmpty() {
      return path.isEmpty();
   }

   @Override
   public List<? extends PathSegment<T>> getPath() {
      return path;
   }

   public void printWithLayer(ObjectLayer layer) {
      var map = layer.getMap();
      var current = new Vector2i();

      for (current.y = 0; current.y < map.getRows(); ++current.y) {
         for (current.x = 0; current.x < map.getColumns(); ++current.x) {
            if (!layer.isTileReachable(current)) {
               System.out.print(" X ");
            } else if (contains(current)) {
               System.out.print(" · ");
            } else {
               System.out.print("   ");
            }
         }
         System.out.println();
      }
   }

   public MovementPath<T> add(Direction direction, int x, int y) {
      path.add(new PositionableMoveSegment<>(direction, x, y));
      return this;
   }

   public MovementPath<T> addFirst(Direction direction, int x, int y) {
      path.add(0, new PositionableMoveSegment<>(direction, x, y));
      return this;
   }

   public boolean contains(Movable movable) {
      var coordinates = movable.getCoordinates();

      for (var segment : path) {
         if (segment.x == coordinates.x() && segment.y == coordinates.y()) {
            return true;
         }
      }

      return false;
   }

   public boolean contains(Vector2ic vector) {
      for (var segment : path) {
         if (segment.x == vector.x() && segment.y == vector.y()) {
            return true;
         }
      }

      return false;
   }

   public boolean contains(int x, int y) {
      for (var segment : path) {
         if (segment.x == x && segment.y == y) {
            return true;
         }
      }

      return false;
   }

   private static class PositionableMoveSegment<T extends Movable> extends MoveSegment<T> {
      final int x;
      final int y;

      PositionableMoveSegment(Direction direction, int x, int y) {
         super(direction);
         this.x = x;
         this.y = y;
      }
   }
}
