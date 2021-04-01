package com.bartlomiejpluta.base.api.input;

@FunctionalInterface
public interface KeyEventHandler {
   void handleKeyEvent(KeyEvent event);

   default void onKeyEventHandlerRegister() {
      // do nothing
   }

   default void onKeyEventHandlerUnregister() {
      // do nothing
   }
}
