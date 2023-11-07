package com.bartlomiejpluta.base.api.map.model;

import com.bartlomiejpluta.base.api.event.Reactive;
import com.bartlomiejpluta.base.api.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.map.layer.color.ColorLayer;
import com.bartlomiejpluta.base.api.map.layer.image.ImageLayer;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.map.layer.tile.TileLayer;
import org.joml.Vector2fc;
import org.joml.Vector3fc;

public interface GameMap extends Reactive {
   float getWidth();

   float getHeight();

   int getRows();

   int getColumns();

   Vector2fc getSize();

   Vector2fc getStepSize();

   Layer getLayer(int layerIndex);

   TileLayer getTileLayer(int layerIndex);

   ImageLayer getImageLayer(int layerIndex);

   ColorLayer getColorLayer(int layerIndex);

   ObjectLayer getObjectLayer(int layerIndex);

   Vector3fc getAmbientColor();

   default void setAmbientColor(Vector3fc color) {
      setAmbientColor(color.x(), color.y(), color.z());
   }

   void setAmbientColor(float red, float green, float blue);
}
