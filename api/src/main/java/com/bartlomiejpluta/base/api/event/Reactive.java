package com.bartlomiejpluta.base.api.event;

import java.util.function.Consumer;

public interface Reactive {
   <E extends Event> void handleEvent(E event);

   <E extends Event> void addEventListener(EventType<E> type, Consumer<E> listener);

   <E extends Event> void removeEventListener(EventType<E> type, Consumer<E> listener);
}
