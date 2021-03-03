package com.bartlomiejpluta.base.api.game.map.layer.object;

import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.entity.Movement;
import com.bartlomiejpluta.base.api.game.map.layer.base.Layer;

import java.util.List;

public interface ObjectLayer extends Layer {
   void addEntity(Entity entity);

   void removeEntity(Entity entity);

   List<Entity> getEntities();

   void setPassageAbility(int row, int column, PassageAbility passageAbility);

   PassageAbility[][] getPassageMap();

   boolean isMovementPossible(Movement movement);
}
