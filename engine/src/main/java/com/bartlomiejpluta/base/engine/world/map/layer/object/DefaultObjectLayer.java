package com.bartlomiejpluta.base.engine.world.map.layer.object;

import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.map.layer.object.PassageAbility;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.api.move.Movement;
import com.bartlomiejpluta.base.api.rule.Rule;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.world.map.layer.base.BaseLayer;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import lombok.Getter;
import lombok.NonNull;
import org.joml.Vector2ic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.lang.Float.compare;
import static java.lang.Integer.compare;

public class DefaultObjectLayer extends BaseLayer implements ObjectLayer {

   @Getter
   private final List<Entity> entities;

   @Getter
   private final PassageAbility[][] passageMap;

   private final Queue<Movement> movements = new LinkedList<>();

   private final List<Rule> rules = new ArrayList<>();

   private final List<Entity> entitiesToAdd = new LinkedList<>();
   private final List<Rule> rulesToAdd = new LinkedList<>();
   private final List<Entity> entitiesToRemove = new LinkedList<>();
   private final List<Rule> rulesToRemove = new LinkedList<>();

   private final int rows;
   private final int columns;

   public DefaultObjectLayer(@NonNull GameMap map, int rows, int columns, List<Entity> entities, PassageAbility[][] passageMap) {
      super(map);
      this.rows = rows;
      this.columns = columns;
      this.entities = entities;
      this.passageMap = passageMap;
   }

   @Override
   public void registerRule(Rule rule) {
      rulesToAdd.add(rule);
   }

   @Override
   public void unregisterRule(Rule rule) {
      rulesToRemove.add(rule);
   }

   @Override
   public void unregisterRules() {
      rulesToRemove.addAll(rules);
   }

   @Override
   public void addEntity(Entity entity) {
      entitiesToAdd.add(entity);
   }

   @Override
   public void removeEntity(Entity entity) {
      entitiesToRemove.add(entity);
   }

   @Override
   public void clearEntities() {
      entitiesToRemove.addAll(entities);
   }

   @Override
   public void setPassageAbility(int row, int column, PassageAbility passageAbility) {
      passageMap[row][column] = passageAbility;
   }

   @Override
   public boolean isTileReachable(Vector2ic tileCoordinates) {
      // Is trying to go beyond the map
      if (tileCoordinates.x() < 0 || tileCoordinates.y() < 0 || tileCoordinates.x() >= columns || tileCoordinates.y() >= rows) {
         return false;
      }

      if (passageMap[tileCoordinates.y()][tileCoordinates.x()] != PassageAbility.ALLOW) {
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
      super.update(dt);

      while (!movements.isEmpty()) {
         var movement = movements.poll();
         if (isTileReachable(movement.getTo())) {
            movement.perform();
         }
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

      // Insert entities requested to be added
      if(!entitiesToAdd.isEmpty()) {
         for (var entity : entitiesToAdd) {
            entity.onAdd(this);
            entity.setStepSize(stepSize.x(), stepSize.y());
            entities.add(entity);
         }

         entitiesToAdd.clear();
      }

      // Insert rules requested to be added
      if(!rulesToAdd.isEmpty()) {
         rules.addAll(rulesToAdd);
         rulesToAdd.clear();
      }

      // Remove entities requested to be removed
      if (!entitiesToRemove.isEmpty()) {
         for (var entity : entitiesToRemove) {
            entities.remove(entity);
            entity.onRemove(this);
         }

         entitiesToRemove.clear();
      }

      // Remove rules requested to be unregistered
      if (!rulesToRemove.isEmpty()) {
         rules.removeAll(rulesToRemove);
         rulesToRemove.clear();
      }
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      entities.sort(this::compareObjects);

      for (var object : entities) {
         object.render(screen, camera, shaderManager);
      }

      super.render(screen, camera, shaderManager);
   }

   private int compareObjects(Entity a, Entity b) {
      var z = compare(a.getZIndex(), b.getZIndex());
      return z == 0 ? compare(a.getPosition().y(), b.getPosition().y()) : z;
   }
}
