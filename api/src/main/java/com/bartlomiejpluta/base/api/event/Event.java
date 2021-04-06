package com.bartlomiejpluta.base.api.event;

public interface Event {
   EventType<? extends Event> getType();

   boolean isConsumed();

   void consume();
}
