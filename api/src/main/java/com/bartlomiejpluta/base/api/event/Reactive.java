package com.bartlomiejpluta.base.api.event;

public interface Reactive {
   <E extends Event> void handleEvent(E event);
}
