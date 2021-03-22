package com.bartlomiejpluta.base.api.map.layer.object;

import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.move.Movement;
import com.bartlomiejpluta.base.api.rule.Rule;
import org.joml.Vector2ic;

import java.util.List;

public interface ObjectLayer extends Layer {
   void addEntity(Entity entity);

   void removeEntity(Entity entity);

   void clearEntities();

   List<Entity> getEntities();

   void registerRule(Rule rule);

   void unregisterRule(Rule rule);

   void unregisterRules();

   void setPassageAbility(int row, int column, PassageAbility passageAbility);

   PassageAbility[][] getPassageMap();

   boolean isTileReachable(Vector2ic tileCoordinates);

   void pushMovement(Movement movement);
}
