package com.bartlomiejpluta.base.api.game.input;

public interface Input {
   boolean isKeyPressed(Key key);

   void addKeyEventHandler(KeyEventHandler handler);

   void removeKeyEventHandler(KeyEventHandler handler);
}
