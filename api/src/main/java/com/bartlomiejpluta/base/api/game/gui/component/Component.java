package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.gui.base.Widget;

public interface Component extends Widget {
   boolean isFocused();

   void focus();

   void blur();
}
