package com.bartlomiejpluta.base.api.gui;

import java.util.concurrent.CompletableFuture;

public interface Window extends Widget {
   void setContent(Component component);

   WindowPosition getWindowPosition();

   void setWindowPosition(WindowPosition windowPosition);

   Component reference(String ref);

   <T extends Component> T reference(String ref, Class<T> type);

   CompletableFuture<Object> getFuture();

   default void onOpen(WindowManager manager, Object[] args) {
      // do nothing
   }

   default void onClose(WindowManager manager) {
      // do nothing
   }
}
