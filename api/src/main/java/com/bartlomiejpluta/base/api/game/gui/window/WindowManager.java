package com.bartlomiejpluta.base.api.game.gui.window;

import com.bartlomiejpluta.base.api.game.gui.base.Widget;

public interface WindowManager extends Widget {
   void setDisplayMode(DisplayMode mode);

   void open(Window window);

   void close();

   int size();

   default void closeAll() {
      for (int i = 0; i < size(); ++i) {
         close();
      }
   }
}
