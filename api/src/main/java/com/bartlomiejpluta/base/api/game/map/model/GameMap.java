package com.bartlomiejpluta.base.api.game.map.model;

import com.bartlomiejpluta.base.api.game.map.layer.color.ColorLayer;
import com.bartlomiejpluta.base.api.game.map.layer.image.ImageLayer;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.game.map.layer.tile.TileLayer;
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

   ObjectLayer getObjectLayer(int layerIndex);
}
