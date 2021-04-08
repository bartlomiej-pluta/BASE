package com.bartlomiejpluta.base.api.map.model;

import com.bartlomiejpluta.base.api.event.Reactive;
import com.bartlomiejpluta.base.api.map.layer.color.ColorLayer;
import com.bartlomiejpluta.base.api.map.layer.image.ImageLayer;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.map.layer.tile.TileLayer;
import org.joml.Vector2fc;

public interface GameMap extends Reactive {
   float getWidth();

   float getHeight();

   int getRows();

   int getColumns();

   Vector2fc getSize();

   Vector2fc getStepSize();

   TileLayer getTileLayer(int layerIndex);

   ImageLayer getImageLayer(int layerIndex);

   ColorLayer getColorLayer(int layerIndex);

   ObjectLayer getObjectLayer(int layerIndex);
}
