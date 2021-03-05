package com.bartlomiejpluta.base.api.game.map.layer.object;

import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.entity.Movement;
import com.bartlomiejpluta.base.api.game.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.game.rule.Rule;

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

   boolean isMovementPossible(Movement movement);
}
