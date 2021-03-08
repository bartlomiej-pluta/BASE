package com.bartlomiejpluta.base.api.util.pathfinder;

import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import org.joml.Vector2i;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.function.Function;

import static java.lang.Math.abs;

public class AstarPathFinder implements PathFinder {

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

   private final int maxNodes;

   public AstarPathFinder(int maxNodes) {
      this.maxNodes = maxNodes;
   }

   @Override
   public LinkedList<Vector2i> findPath(ObjectLayer layer, Vector2i start, Vector2i end) {
      var columns = layer.getMap().getColumns();
      var rows = layer.getMap().getRows();

      var startNode = new Node(start);
      var endNode = new Node(end);

      // The heuristic function defined as Manhattan distance to the end node
      Function<Node, Float> h = node -> manhattanDistance(node.position, end);

      // The start node has the actual cost 0 and estimated is a Manhattan distance to the end node
      startNode.g = 0.0f;
      startNode.f = h.apply(startNode);

      // We are starting with one open node (the start one) end empty closed lists
      var open = new PriorityQueue<Node>();
      var closed = new HashSet<Node>();
      open.add(startNode);

      // As long as there are at least one open node
      while (!open.isEmpty()) {

         // A safety valve which ideally should be used only and only
         // if the target is not reachable (the path does not exist at all).
         // It determines the maximum algorithm depth
         // It's not the part of model A* algorithm.
         if (closed.size() > maxNodes) {
            return new LinkedList<>();
         }

         // We are retrieving the node with the **smallest** f score
         // (That's the way the Astar.compare() comparator works)
         // And the same time we are removing the node from open list
         // and pushing it to closed one as we no longer need to analyze this node
         var current = open.poll();
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
            var position = new Vector2i(current.position).add(adjacent);

            // We are getting rid the neighbours beyond the map
            if (position.x < 0 || position.x >= columns || position.y < 0 || position.y >= rows) {
               continue;
            }

            // We are limiting the algorithm to given range
            // If current neighbour distance to start node exceeds given range parameter
            // we are getting rid of this neighbour
            //if (manhattanDistance(startNode.position, position) > range) {
            //   continue;
            //}

            // Define new neighbour
            var neighbour = new Node(position);

            // If we already analyzed this node,
            // we are free to skip it to not analyze it once again
            for (var closedNode : closed) {
               if (closedNode.position.equals(position)) {
                  continue adjacent;
               }
            }

            // Get rid of nodes that are not reachable (blocked or something is staying on there)
            //
            // ASSUME, that the end tile **always is** reachable, even if actually it is not.
            // That means, if you want to have empty list if target actually is not reachable,
            // you need to implement that condition by yourself.
            // The reason for that is the fact, that when ObjectLayer is checking if
            // the current tile is reachable via isTileReachable() method.
            // If the target position is actually an entity which is blocking (does not allow other entities pass
            // through it), the method rejects the end tile as reachable (because de facto it is not reachable since
            // it is blocking) and eventually the path is assumed as not existing.
            // It may not be consistent with a A* model implementation, however it is required to adapt
            // the algorithm for the BASE project purpose.
            var reachable = layer.isTileReachable(position) || position.equals(end);
            if (!reachable) {
               continue;
            }

            // We are evaluating the basic A* parameters
            // as well as the parent node which is needed to recreate
            // path further
            neighbour.parent = current;
            neighbour.g = current.g + 1;
            neighbour.f = neighbour.g + h.apply(neighbour);

            // If the node already exists in open list,
            // we need to compare current neighbour with existing node
            // to check which path is shorter.
            // If the neighbour is shorter, we can update the existing node
            // with neighbour's parameters
            for (Object openNode : open) {
               var node = (Node) openNode;
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

   private float manhattanDistance(Vector2i a, Vector2i b) {
      return (abs(a.x - b.x) + abs(a.y - b.y));
   }

   private LinkedList<Vector2i> recreatePath(Node node) {
      var current = node;
      var list = new LinkedList<Vector2i>();

      while (current.parent != null) {
         list.add(current.position);
         current = current.parent;
      }

      return list;
   }

   private static class Node implements Comparable<Node> {
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
         var node = (Node) o;
         return position.equals(node.position);
      }

      @Override
      public int hashCode() {
         return Objects.hash(position);
      }

      @Override
      public int compareTo(Node o) {
         return Float.compare(f, o.f);
      }
   }
}