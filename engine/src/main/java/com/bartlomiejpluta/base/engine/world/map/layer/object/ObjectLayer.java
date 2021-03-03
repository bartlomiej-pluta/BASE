package com.bartlomiejpluta.base.engine.world.map.layer.object;

import com.bartlomiejpluta.base.api.game.entity.Direction;
import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.map.PassageAbility;
import com.bartlomiejpluta.base.api.internal.camera.Camera;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.api.internal.window.Window;
import com.bartlomiejpluta.base.engine.world.map.layer.base.Layer;
import org.joml.Vector2i;

import java.util.List;

public class ObjectLayer implements Layer {
   private final List<Entity> objects;

   private final PassageAbility[][] passageMap;

   public ObjectLayer(List<Entity> objects, PassageAbility[][] passageMap) {
      this.objects = objects;
      this.passageMap = passageMap;
   }

   public void addObject(Entity object) {
      objects.add(object);
   }

   public void removeObject(Entity object) {
      objects.remove(object);
   }

   public void setPassageAbility(int row, int column, PassageAbility passageAbility) {
      passageMap[row][column] = passageAbility;
   }

   public boolean isMovementPossible(Vector2i source, Vector2i target, Direction direction) {
      var isTargetReachable = switch (passageMap[target.y][target.x]) {
         case UP_ONLY -> direction != Direction.DOWN;
         case DOWN_ONLY -> direction != Direction.UP;
         case LEFT_ONLY -> direction != Direction.RIGHT;
         case RIGHT_ONLY -> direction != Direction.LEFT;
         case BLOCK -> false;
         case ALLOW -> true;
      };

      var canMoveFromCurrentTile = switch (passageMap[source.y][source.x]) {
         case UP_ONLY -> direction == Direction.UP;
         case DOWN_ONLY -> direction == Direction.DOWN;
         case LEFT_ONLY -> direction == Direction.LEFT;
         case RIGHT_ONLY -> direction == Direction.RIGHT;
         default -> true;
      };

      return isTargetReachable && canMoveFromCurrentTile;
   }

   @Override
   public void render(Window window, Camera camera, ShaderManager shaderManager) {
      objects.sort(this::compareObjects);

      for (var object : objects) {
         object.render(window, camera, shaderManager);
      }
   }

   private int compareObjects(Entity a, Entity b) {
      return Float.compare(a.getPosition().y, b.getPosition().y);
   }

   @Override
   public void update(float dt) {
      for (var object : objects) {
         object.update(dt);
      }
   }
}
