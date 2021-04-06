package com.bartlomiejpluta.base.api.map.layer.object;

import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.event.Reactive;
import com.bartlomiejpluta.base.api.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.move.Movement;
import org.joml.Vector2ic;

import java.util.List;

public interface ObjectLayer extends Layer, Reactive {
   void addEntity(Entity entity);

   void removeEntity(Entity entity);

   void clearEntities();

   List<Entity> getEntities();

   void setPassageAbility(int row, int column, PassageAbility passageAbility);

   PassageAbility[][] getPassageMap();

   boolean isTileReachable(Vector2ic tileCoordinates);

   void pushMovement(Movement movement);
}
