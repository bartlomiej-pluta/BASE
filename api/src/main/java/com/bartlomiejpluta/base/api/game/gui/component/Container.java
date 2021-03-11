package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.gui.base.Widget;

public interface Container extends Widget {
   void add(Component component);

   void remove(Component component);
}
