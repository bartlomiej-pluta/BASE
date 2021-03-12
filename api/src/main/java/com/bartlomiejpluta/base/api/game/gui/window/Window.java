package com.bartlomiejpluta.base.api.game.gui.window;

import com.bartlomiejpluta.base.api.game.gui.base.Widget;

public interface Window extends Widget {
   WindowPosition getWindowPosition();

   void setWindowPosition(WindowPosition windowPosition);

   void onOpen(WindowManager manager);

   void onClose(WindowManager manager);
}