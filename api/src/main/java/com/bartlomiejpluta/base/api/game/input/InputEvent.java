package com.bartlomiejpluta.base.api.game.input;

public interface InputEvent {
   boolean isConsumed();

   void consume();
}
