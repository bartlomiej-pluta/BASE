package com.bartlomiejpluta.base.core.world.map;

import com.bartlomiejpluta.base.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.world.camera.Camera;
import com.bartlomiejpluta.base.core.world.movement.Direction;
import com.bartlomiejpluta.base.core.world.movement.MovableObject;
import org.joml.Vector2i;

import java.util.List;

public class ObjectLayer implements Layer {
   private final List<MovableObject> objects;

   private final PassageAbility[][] passageMap;

   public ObjectLayer(List<MovableObject> objects, PassageAbility[][] passageMap) {
      this.objects = objects;
      this.passageMap = passageMap;
   }

   public void addObject(MovableObject object) {
      objects.add(object);
   }

   public void removeObject(MovableObject object) {
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
      for (var object : objects) {
         object.render(window, camera, shaderManager);
      }
   }

   @Override
   public void update(float dt) {
      for (var object : objects) {
         object.update(dt);
      }
   }
}
