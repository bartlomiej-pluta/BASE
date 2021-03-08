package com.bartlomiejpluta.base.engine.world.map.layer.object;

import com.bartlomiejpluta.base.api.game.ai.NPC;
import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.entity.Movement;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.game.map.layer.object.PassageAbility;
import com.bartlomiejpluta.base.api.game.map.model.GameMap;
import com.bartlomiejpluta.base.api.game.rule.Rule;
import com.bartlomiejpluta.base.api.game.window.Window;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import lombok.Getter;
import lombok.NonNull;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DefaultObjectLayer implements ObjectLayer {

   @Getter
   private final GameMap map;

   @Getter
   private final List<Entity> entities;

   @Getter
   private final PassageAbility[][] passageMap;

   private final List<Movement> movements = new LinkedList<>();

   private final List<Rule> rules = new ArrayList<>();

   private final int rows;
   private final int columns;
   private final Vector2f stepSize;

   public DefaultObjectLayer(@NonNull GameMap map, int rows, int columns, @NonNull Vector2f stepSize, List<Entity> entities, PassageAbility[][] passageMap) {
      this.map = map;
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
      entity.onAdd(this);
      entity.setStepSize(stepSize.x, stepSize.y);
      entities.add(entity);
   }

   @Override
   public void removeEntity(Entity entity) {
      entity.onRemove(this);
      entities.remove(entity);
   }

   @Override
   public void clearEntities() {
      entities.clear();
   }

   @Override
   public void setPassageAbility(int row, int column, PassageAbility passageAbility) {
      passageMap[row][column] = passageAbility;
   }

   @Override
   public boolean isTileReachable(Vector2i tileCoordinates) {
      // Is trying to go beyond the map
      if (tileCoordinates.x < 0 || tileCoordinates.y < 0 || tileCoordinates.x >= columns || tileCoordinates.y >= rows) {
         return false;
      }

      if (passageMap[tileCoordinates.y][tileCoordinates.x] != PassageAbility.ALLOW) {
         return false;
      }


      for (var entity : entities) {
         if (entity.isBlocking()) {

            // The tile is occupied by other entity
            if (entity.getCoordinates().equals(tileCoordinates)) {
               return false;
            }

            // The tile is empty, however another entity is moving on it
            var otherMovement = entity.getMovement();
            if (otherMovement != null && otherMovement.getTo().equals(tileCoordinates)) {
               return false;
            }
         }
      }


      return true;
   }

   @Override
   public void pushMovement(Movement movement) {
      movements.add(movement);
   }

   @Override
   public void update(float dt) {
      for (var iterator = movements.iterator(); iterator.hasNext(); ) {
         var movement = iterator.next();

         if (isTileReachable(movement.getTo())) {
            movement.perform();
         }

         iterator.remove();
      }

      for (var entity : entities) {
         for (var rule : rules) {
            if (rule.when(entity)) {
               rule.then(entity);
            }
         }

         entity.update(dt);

         if (entity instanceof NPC) {
            ((NPC) entity).getStrategy().nextActivity(this, dt);
         }
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
