package com.bartlomiejpluta.base.api.map.layer.object;

import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.move.Movement;
import com.bartlomiejpluta.base.api.rule.MovementRule;
import org.joml.Vector2ic;

import java.util.List;

public interface ObjectLayer extends Layer {
   void addEntity(Entity entity);

   void removeEntity(Entity entity);

   void clearEntities();

   List<Entity> getEntities();

   void registerMovementRule(MovementRule rule);

   void unregisterMovementRule(MovementRule rule);

   void unregisterRules();

   void setPassageAbility(int row, int column, PassageAbility passageAbility);

   PassageAbility[][] getPassageMap();

   boolean isTileReachable(Vector2ic tileCoordinates);

   void pushMovement(Movement movement);

   // Notifiers
   void notifyEntityStepIn(Movement movement, Entity entity);

   void notifyEntityStepOut(Movement movement, Entity entity);
}
