package com.bartlomiejpluta.base.engine.pathfinding;

import com.bartlomiejpluta.base.api.game.map.layer.color.ColorLayer;
import com.bartlomiejpluta.base.api.game.map.layer.image.ImageLayer;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.game.map.layer.object.PassageAbility;
import com.bartlomiejpluta.base.api.game.map.layer.tile.TileLayer;
import com.bartlomiejpluta.base.api.game.map.model.GameMap;
import com.bartlomiejpluta.base.engine.world.map.layer.object.DefaultObjectLayer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.*;
import java.util.function.Function;

import static java.lang.Math.abs;

@SuppressWarnings("ConstantConditions")
@AllArgsConstructor
public class Astar implements Comparator<Astar.Node> {

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

   public List<Node> findPath(ObjectLayer layer, Node start, Node end) {
      int columns = layer.getMap().getColumns();
      int rows = layer.getMap().getRows();

      // The heuristic function defined as Manhattan distance to the end node
      var h = createManhattanDistanceHeuristic(end);

      // The start node has the actual cost 0 and estimated is a Manhattan distance to the end node
      start.g = 0.0f;
      start.f = h.apply(start);

      // We are starting with one open node (the start one) end empty closed lists
      var open = new PriorityQueue<>(this);
      var closed = new LinkedList<Node>();
      open.add(start);

      // As long as there are at least one open node
      while (!open.isEmpty()) {

         // We are retrieving the node with the **smallest** f score
         // (That's the way the Astar.compare() comparator works)
         // And the same time we are removing the node from open list
         // and pushing it to closed one as we no longer need to analyze this node
         var current = open.poll();
         closed.add(current);

         // If we found the node with f score and it is
         // actually an end node, we have most likely found a best path
         if (current.equals(end)) {
            return recreatePath(current);
         }

         adjacent:
         // For each node neighbour
         // (we are analyzing the 4 neighbours,
         // as described in the commend above ADJACENT static field)
         for (var adjacent : ADJACENT) {
            var position = new Vector2i(current.position).add(adjacent);

            // We are getting rid the neighbours beyond the map
            if (position.x < 0 || position.x >= columns || position.y < 0 || position.y >= rows) {
               continue;
            }

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
            var reachable = layer.getPassageMap()[position.y][position.x] == PassageAbility.ALLOW;
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
            for (var openNode : open) {
               if (openNode.position.equals(position) && neighbour.g < openNode.g) {
                  openNode.g = neighbour.g;
                  openNode.parent = current;
                  continue adjacent;
               }
            }

            // Push neighbour to open list to consider it later
            open.add(neighbour);
         }
      }

      // If open list is empty and we didn't reach the end node
      // it means that the path probably does not exist at all
      return Collections.emptyList();
   }

   @SuppressWarnings("Convert2Lambda")
   private Function<Node, Float> createManhattanDistanceHeuristic(Node toNode) {
      return new Function<>() {

         @Override
         public Float apply(Node node) {
            return (float) (abs(toNode.position.x - node.position.x) + abs(toNode.position.y - node.position.y));
         }
      };
   }

   private List<Node> recreatePath(Node node) {
      var current = node;
      var list = new ArrayList<Node>();
      list.add(node);

      while (current.parent != null) {
         list.add(current.parent);
         current = current.parent;
      }

      return list;
   }

   private void print(ObjectLayer layer, Iterable<Node> nodes) {
      for (int row = 0; row < layer.getMap().getRows(); ++row) {
         System.out.print("|");

         tiles:
         for (int column = 0; column < layer.getMap().getColumns(); ++column) {

            for (var node : nodes) {
               if (node.position.equals(column, row)) {
                  System.out.print(" # ");
                  continue tiles;
               }
            }

            System.out.print(layer.getPassageMap()[row][column] == PassageAbility.ALLOW ? "   " : " . ");
         }

         System.out.println("|");
      }
   }

   @Override
   public int compare(Node o1, Node o2) {
      return Float.compare(o1.f, o2.f);
   }

   @EqualsAndHashCode(of = "position")
   @RequiredArgsConstructor
   protected static class Node {
      public Node parent;
      public final Vector2i position;
      public float g = 0.0f;
      public float f = 0.0f;
   }

   public static void main(String[] args) {
      final int rows = 50;
      final int columns = 50;
      final int threshold = 70;
      var start = new Vector2i(1, 1);
      var end = new Vector2i(49, 49);


      final Random random = new Random();
      final Vector2f stepSize = new Vector2f(32, 32);

      var passageMap = new PassageAbility[rows][columns];
      for (int i = 0; i < rows; ++i) {
         passageMap[i] = new PassageAbility[columns];

         for (int j = 0; j < columns; ++j) {
            passageMap[i][j] = random.nextInt(100) >= threshold ? PassageAbility.BLOCK : PassageAbility.ALLOW;
         }
      }

      var map = new GameMap() {

         @Override
         public float getWidth() {
            return 0;
         }

         @Override
         public float getHeight() {
            return 0;
         }

         @Override
         public int getRows() {
            return rows;
         }

         @Override
         public int getColumns() {
            return columns;
         }

         @Override
         public Vector2f getSize() {
            return null;
         }

         @Override
         public TileLayer getTileLayer(int layerIndex) {
            return null;
         }

         @Override
         public ImageLayer getImageLayer(int layerIndex) {
            return null;
         }

         @Override
         public ColorLayer getColorLayer(int layerIndex) {
            return null;
         }

         @Override
         public ObjectLayer getObjectLayer(int layerIndex) {
            return null;
         }
      };

      var layer = new DefaultObjectLayer(map, rows, columns, stepSize, new ArrayList<>(), passageMap);

      var astar = new Astar();

      passageMap[start.y][start.x] = PassageAbility.ALLOW;
      passageMap[end.y][end.x] = PassageAbility.ALLOW;

      var time = System.currentTimeMillis();
      var output = astar.findPath(layer, new Node(start), new Node(end));
      var elapsed = System.currentTimeMillis() - time;
      astar.print(layer, output);
      System.out.println("Time: " + elapsed + "ms");
   }
}
