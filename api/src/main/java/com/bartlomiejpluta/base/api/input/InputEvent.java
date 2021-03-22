package com.bartlomiejpluta.base.api.input;

public interface InputEvent {
   boolean isConsumed();

   void consume();
}
