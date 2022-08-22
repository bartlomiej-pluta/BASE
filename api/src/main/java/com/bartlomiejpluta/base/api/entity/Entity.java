package com.bartlomiejpluta.base.api.entity;

import com.bartlomiejpluta.base.api.event.Reactive;
import com.bartlomiejpluta.base.api.location.Locationable;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.internal.logic.Updatable;
import com.bartlomiejpluta.base.internal.render.Renderable;

public interface Entity extends Locationable, Renderable, Updatable, Reactive {
   boolean isBlocking();

   void setBlocking(boolean blocking);

   int getZIndex();

   void setZIndex(int zIndex);

   ObjectLayer getLayer();

   void onAdd(ObjectLayer layer);

   void onRemove(ObjectLayer layer);
}
