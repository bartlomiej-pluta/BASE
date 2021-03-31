package com.bartlomiejpluta.base.engine.world.map.layer.object;

import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.entity.InteractiveEntity;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.map.layer.object.PassageAbility;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.api.move.Movement;
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
   private final ArrayList<Entity> entities = new ArrayList<>();

   private final List<InteractiveEntity> interactiveEntities = new ArrayList<>();

   @Getter
   private final PassageAbility[][] passageMap;

   private final Queue<Movement> movements = new LinkedList<>();

   private final int rows;
   private final int columns;

   public DefaultObjectLayer(@NonNull GameMap map, int rows, int columns, PassageAbility[][] passageMap) {
      super(map);
      this.rows = rows;
      this.columns = columns;
      this.passageMap = passageMap;
   }

   @Override
   public void addEntity(Entity entity) {
      var layer = (DefaultObjectLayer) entity.getLayer();

      if (layer != null) {
         layer.entities.remove(entity);
      }

      if (entity instanceof InteractiveEntity) {
         interactiveEntities.add((InteractiveEntity) entity);
      }

      entity.setStepSize(stepSize.x(), stepSize.y());
      entities.add(entity);

      entity.onAdd(this);
   }

   @Override
   public void removeEntity(Entity entity) {
      entities.remove(entity);

      if (entity instanceof InteractiveEntity) {
         interactiveEntities.remove(entity);
      }

      entity.onRemove(this);
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

   @SuppressWarnings("ForLoopReplaceableByForEach")
   @Override
   public void update(float dt) {
      // Update BaseLayer (animations etc.)
      super.update(dt);

      while (!movements.isEmpty()) {
         var movement = movements.poll();
         var from = movement.getFrom();
         var to = movement.getTo();
         if (isTileReachable(to)) {
            movement.perform();
         }
      }

      // Disclaimer
      // For the sake of an easy adding and removing
      // entities from the entity.update() method inside
      // the loop, the loop itself has been implemented
      // as plain old C-style for loop.
      for (int i = 0; i < entities.size(); ++i) {
         var entity = entities.get(i);
         entity.update(dt);

         if (entity instanceof NPC) {
            ((NPC) entity).getStrategy().nextActivity(this, dt);
         }
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

   @Override
   public void notifyEntityStepIn(Movement movement, Entity entity) {
      var target = movement.getTo();

      for (var listener : interactiveEntities) {
         if (listener.getCoordinates().equals(target)) {
            listener.onEntityStepIn(movement, entity);
         }
      }
   }

   @Override
   public void notifyEntityStepOut(Movement movement, Entity entity) {
      var source = movement.getFrom();

      for (var listener : interactiveEntities) {
         if (listener.getCoordinates().equals(source)) {
            listener.onEntityStepOut(movement, entity);
         }
      }
   }
}
