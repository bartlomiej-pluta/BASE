package com.bartlomiejpluta.base.engine.world.map.layer.object;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.entity.Direction;
import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.entity.Movement;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.game.map.layer.object.PassageAbility;
import com.bartlomiejpluta.base.api.game.rule.Rule;
import com.bartlomiejpluta.base.api.game.window.Window;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import lombok.Getter;
import lombok.NonNull;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class DefaultObjectLayer implements ObjectLayer {

   @Getter
   private final List<Entity> entities;

   @Getter
   private final PassageAbility[][] passageMap;

   private final List<Rule> rules = new ArrayList<>();

   private final int rows;
   private final int columns;
   private final Vector2f stepSize;

   public DefaultObjectLayer(int rows, int columns, @NonNull Vector2f stepSize, List<Entity> entities, PassageAbility[][] passageMap) {
      this.rows = rows;
      this.columns = columns;
      this.stepSize = stepSize;
      this.entities = entities;
      this.passageMap = passageMap;
   }

   @Override
   public void registerRule(Rule rule) {
      rules.add(rule);
   }

   @Override
   public void unregisterRule(Rule rule) {
      rules.remove(rule);
   }

   @Override
   public void unregisterRules() {
      rules.clear();
   }

   @Override
   public void addEntity(Entity entity) {
      entity.setStepSize(stepSize.x, stepSize.y);
      entities.add(entity);
   }

   @Override
   public void removeEntity(Entity entity) {
      entities.remove(entity);
   }

   @Override
   public void setPassageAbility(int row, int column, PassageAbility passageAbility) {
      passageMap[row][column] = passageAbility;
   }

   @Override
   public boolean isMovementPossible(Movement movement) {
      var target = movement.getTo();

      // Is trying to go beyond the map
      if (target.x < 0 || target.y < 0 || target.x >= columns || target.y >= rows) {
         return false;
      }

      var source = movement.getFrom();
      var direction = movement.getDirection();

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
   public void update(float dt) {
      for (var entity : entities) {
         for (var rule : rules) {
            if (rule.when(entity)) {
               rule.then(entity);
            }
         }

         entity.update(dt);
      }
   }

   @Override
   public void render(Window window, Camera camera, ShaderManager shaderManager) {
      entities.sort(this::compareObjects);

      for (var object : entities) {
         object.render(window, camera, shaderManager);
      }
   }

   private int compareObjects(Entity a, Entity b) {
      return Float.compare(a.getPosition().y, b.getPosition().y);
   }
}
