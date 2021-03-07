package com.bartlomiejpluta.base.api.game.ai.pathfinding;

import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.game.map.layer.object.PassageAbility;
import org.joml.Vector2i;

import java.util.*;
import java.util.function.Function;

import static java.lang.Math.abs;

@SuppressWarnings({"RedundantCast", "rawtypes"})
public class Astar implements PathFinder {

   /*
    * We are interested in following adjacent
    *  +---+---+---+
    *  |   | o |   |
    *  +---+---+---+
    *  | o | x | o |
    *  +---+---+---+
    *  |   | o |   |
    *  +---+---+---+
    */
   private static final Vector2i[] ADJACENT = new Vector2i[]{
         new Vector2i(-1, 0),
         new Vector2i(0, -1),
         new Vector2i(1, 0),
         new Vector2i(0, 1)
   };

   @Override
   public List<Vector2i> findPath(ObjectLayer layer, Vector2i start, Vector2i end, int range) {
      int columns = layer.getMap().getColumns();
      int rows = layer.getMap().getRows();

      Node startNode = new Node(start);
      Node endNode = new Node(end);

      // The heuristic function defined as Manhattan distance to the end node
      Function h = createManhattanDistanceHeuristic(endNode);

      // The start node has the actual cost 0 and estimated is a Manhattan distance to the end node
      startNode.g = 0.0f;
      startNode.f = (Float) h.apply(startNode);

      // We are starting with one open node (the start one) end empty closed lists
      Queue open = new PriorityQueue();
      List closed = new LinkedList();
      open.add(startNode);

      // As long as there are at least one open node
      while (!open.isEmpty()) {

         // We are retrieving the node with the **smallest** f score
         // (That's the way the Astar.compare() comparator works)
         // And the same time we are removing the node from open list
         // and pushing it to closed one as we no longer need to analyze this node
         Node current = (Node) open.poll();
         closed.add(current);

         // If we found the node with f score and it is
         // actually an end node, we have most likely found a best path
         if (current.equals(endNode)) {
            return recreatePath(current);
         }

         adjacent:
         // For each node neighbour
         // (we are analyzing the 4 neighbours,
         // as described in the commend above ADJACENT static field)
         for (Vector2i adjacent : ADJACENT) {
            Vector2i position = new Vector2i(current.position).add(adjacent);

            // We are getting rid the neighbours beyond the map
            if (position.x < 0 || position.x >= columns || position.y < 0 || position.y >= rows) {
               continue;
            }

            // We are limiting the algorithm to given range
            // If current neighbour distance to start node exceeds given range parameter
            // we are getting rid of this neighbour
            if (manhattanDistance(startNode.position, position) > range) {
               continue;
            }

            // Define new neighbour
            Node neighbour = new Node(position);

            // If we already analyzed this node,
            // we are free to skip it to not analyze it once again
            for (Object closedNode : closed) {
               if (((Node) closedNode).position.equals(position)) {
                  continue adjacent;
               }
            }

            // Get rid of nodes that are not reachable (blocked or something is staying on there)
            boolean reachable = layer.getPassageMap()[position.y][position.x] == PassageAbility.ALLOW;
            if (!reachable) {
               continue;
            }

            // We are evaluating the basic A* parameters
            // as well as the parent node which is needed to recreate
            // path further
            neighbour.parent = current;
            neighbour.g = current.g + 1;
            neighbour.f = neighbour.g + (Float) h.apply(neighbour);

            // If the node already exists in open list,
            // we need to compare current neighbour with existing node
            // to check which path is shorter.
            // If the neighbour is shorter, we can update the existing node
            // with neighbour's parameters
            for (Object openNode : open) {
               Node node = (Node) openNode;
               if (node.position.equals(position) && neighbour.g < node.g) {
                  node.g = neighbour.g;
                  node.parent = current;
                  continue adjacent;
               }
            }

            // Push neighbour to open list to consider it later
            open.add(neighbour);
         }
      }

      // If open list is empty and we didn't reach the end node
      // it means that the path probably does not exist at all
      return new LinkedList<>();
   }

   @SuppressWarnings("Convert2Lambda")
   private Function createManhattanDistanceHeuristic(final Node toNode) {
      return new Function() {

         @Override
         public Object apply(Object node) {
            return manhattanDistance(toNode.position, ((Node) node).position);
         }
      };
   }

   private float manhattanDistance(Vector2i a, Vector2i b) {
      return (abs(a.x - b.x) + abs(a.y - b.y));
   }

   private List<Vector2i> recreatePath(Node node) {
      Node current = node;
      List<Vector2i> list = new LinkedList<>();
      list.add(((Node) node).position);

      while (current.parent != null) {
         list.add(((Node) current).parent.position);
         current = current.parent;
      }

      return list;
   }

   public void print(ObjectLayer layer, Iterable<Vector2i> nodes) {
      for (int row = 0; row < layer.getMap().getRows(); ++row) {
         System.out.print("|");

         tiles:
         for (int column = 0; column < layer.getMap().getColumns(); ++column) {

            for (Object node : nodes) {
               if (((Vector2i) node).equals(column, row)) {
                  System.out.print("#");
                  continue tiles;
               }
            }

            System.out.print(layer.getPassageMap()[row][column] == PassageAbility.ALLOW ? " " : ".");
         }

         System.out.println("|");
      }
   }

   private static class Node implements Comparable {
      public Node parent;
      public final Vector2i position;
      public float g = 0.0f;
      public float f = 0.0f;

      public Node(Vector2i position) {
         this.position = position;
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;
         Node node = (Node) o;
         return position.equals(node.position);
      }

      @Override
      public int hashCode() {
         return Objects.hash(position);
      }

      @Override
      public int compareTo(Object o) {
         return Float.compare(f, ((Node) o).f);
      }
   }
}
