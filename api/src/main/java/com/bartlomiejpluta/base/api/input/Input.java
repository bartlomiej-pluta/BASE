package com.bartlomiejpluta.base.api.input;

public interface Input {
   boolean isKeyPressed(Key key);

   void addKeyEventHandler(KeyEventHandler handler);

   void removeKeyEventHandler(KeyEventHandler handler);
}
