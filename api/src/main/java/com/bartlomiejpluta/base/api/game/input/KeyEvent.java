package com.bartlomiejpluta.base.api.game.input;

public interface KeyEvent extends InputEvent {
   Key getKey();

   KeyAction getAction();
}
