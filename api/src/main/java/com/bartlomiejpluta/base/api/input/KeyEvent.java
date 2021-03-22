package com.bartlomiejpluta.base.api.input;

public interface KeyEvent extends InputEvent {
   Key getKey();

   KeyAction getAction();
}
