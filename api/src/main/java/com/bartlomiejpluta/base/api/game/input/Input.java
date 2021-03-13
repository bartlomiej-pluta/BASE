package com.bartlomiejpluta.base.api.game.input;

import java.util.function.Consumer;

public interface Input {
   boolean isKeyPressed(Key key);

   void addKeyEventHandler(Consumer<KeyEvent> handler);

   void removeKeyEventHandler(Consumer<KeyEvent> handler);
}
