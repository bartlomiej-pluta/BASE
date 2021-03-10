package com.bartlomiejpluta.base.api.game.gui;

import com.bartlomiejpluta.base.api.game.screen.Screen;

public interface Widget {
   Widget getParent();

   void draw(Screen screen, GUI gui);
}
