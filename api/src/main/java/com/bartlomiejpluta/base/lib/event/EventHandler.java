package com.bartlomiejpluta.base.lib.event;

import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.event.EventType;
import com.bartlomiejpluta.base.api.event.Reactive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class EventHandler implements Reactive {
   private final Map<EventType<?>, ArrayList<Consumer<? extends Event>>> listeners = new HashMap<>();

   @SuppressWarnings("unchecked")
   @Override
   public <E extends Event> void handleEvent(E event) {
      var list = listeners.get(event.getType());
      if (list != null) {
         for (var i = list.size() - 1; i >= 0; --i) {
            if (event.isConsumed()) {
               return;
            }

            ((Consumer<E>) list.get(i)).accept(event);
         }
      }
   }

   public <E extends Event> void addListener(EventType<E> type, Consumer<E> listener) {
      var list = this.listeners.get(type);

      if (list != null) {
         list.add(listener);
      } else {
         list = new ArrayList<>();
         list.add(listener);
         listeners.put(type, list);
      }
   }

   public <E extends Event> void removeListener(EventType<E> type, Consumer<E> listener) {
      var list = this.listeners.get(type);

      if (list != null) {
         list.remove(listener);
         if (list.isEmpty()) {
            this.listeners.remove(type);
         }
      }
   }
}
