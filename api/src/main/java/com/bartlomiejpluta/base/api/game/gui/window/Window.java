package com.bartlomiejpluta.base.api.game.gui.window;

import com.bartlomiejpluta.base.api.game.gui.base.Widget;
import com.bartlomiejpluta.base.api.game.gui.component.Component;

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
