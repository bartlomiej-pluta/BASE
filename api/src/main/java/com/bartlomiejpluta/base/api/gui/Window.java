package com.bartlomiejpluta.base.api.gui;

public interface Window extends Widget {
   void setContent(Component component);

   WindowPosition getWindowPosition();

   void setWindowPosition(WindowPosition windowPosition);

   default void onOpen(WindowManager manager) {
      // do nothing
   }

   default void onClose(WindowManager manager) {
      // do nothing
   }
}
