package com.bartlomiejpluta.base.api.game.map;

import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.entity.Movement;
import org.joml.Vector2f;

public interface GameMap {
   float getWidth();

   float getHeight();

   int getRows();

   int getColumns();

   Vector2f getSize();

   TileLayer getTileLayer(int layerIndex);

   ImageLayer getImageLayer(int layerIndex);

   ColorLayer getColorLayer(int layerIndex);

   void addEntity(int objectLayerIndex, Entity entity);

   void removeEntity(int objectLayerIndex, Entity entity);

   boolean isMovementPossible(int objectLayerIndex, Movement movement);

   void setPassageAbility(int objectLayerIndex, int row, int column, PassageAbility passageAbility);
}
