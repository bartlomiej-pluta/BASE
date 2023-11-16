package com.bartlomiejpluta.base.api.entity;

import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.event.EventType;
import com.bartlomiejpluta.base.api.event.Reactive;
import com.bartlomiejpluta.base.api.location.Locationable;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.internal.program.Updatable;
import com.bartlomiejpluta.base.internal.render.Renderable;

import java.util.function.Consumer;

public interface Entity extends Locationable, Renderable, Updatable, Reactive {
   boolean isBlocking();

   void setBlocking(boolean blocking);

   int getZIndex();

   void setZIndex(int zIndex);

   ObjectLayer getLayer();

   void onAdd(ObjectLayer layer);

   void onRemove(ObjectLayer layer);

   <E extends Event> void addEventListener(EventType<E> type, Consumer<E> listener);

   <E extends Event> void removeEventListener(EventType<E> type, Consumer<E> listener);
}
