package com.bartlomiejpluta.base.api.game.map;

import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.entity.Movement;
import org.joml.Vector2f;

public interface GameMap {
   Vector2f getSize();

   void addEntity(int objectLayerIndex, Entity entity);

   void removeEntity(int objectLayerIndex, Entity entity);

   boolean isMovementPossible(int objectLayerIndex, Movement movement);

   void setPassageAbility(int objectLayerIndex, int row, int column, PassageAbility passageAbility);

   void setTile(int tileLayerIndex, int row, int column, int tileId);

   void setTile(int tileLayerIndex, int row, int column, int tileSetRow, int tileSetColumn);

   void clearTile(int tileLayerIndex, int row, int column);

   void setColor(int colorLayerIndex, float r, float g, float b, float alpha);
}
