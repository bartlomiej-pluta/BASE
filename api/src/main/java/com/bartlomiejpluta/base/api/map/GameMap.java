package com.bartlomiejpluta.base.api.map;

import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.entity.Movement;

public interface GameMap {
   void addEntity(int objectLayerIndex, Entity entity);

   void removeEntity(int objectLayerIndex, Entity entity);

   boolean isMovementPossible(int objectLayerIndex, Movement movement);

   void setTile(int tileLayerIndex, int row, int column, int tileId);

   void setTile(int tileLayerIndex, int row, int column, int tileSetRow, int tileSetColumn);

   void clearTile(int tileLayerIndex, int row, int column);

   void setPassageAbility(int objectLayerIndex, int row, int column, PassageAbility passageAbility);

   void setColor(int colorLayerIndex, float r, float g, float b, float alpha);
}
