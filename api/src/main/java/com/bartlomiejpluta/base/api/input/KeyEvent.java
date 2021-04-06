package com.bartlomiejpluta.base.api.input;

import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.event.EventType;

public interface KeyEvent extends Event {
   Key getKey();

   KeyAction getAction();

   EventType<KeyEvent> TYPE = new EventType<>("KEY_EVENT");
}
