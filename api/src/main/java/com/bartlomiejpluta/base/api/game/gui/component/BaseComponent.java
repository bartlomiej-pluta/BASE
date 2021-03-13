package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.gui.base.BaseWidget;

public abstract class BaseComponent extends BaseWidget implements Component {
   protected boolean focused;

   @Override
   public boolean isFocused() {
      return focused;
   }

   @Override
   public void focus() {
      focused = true;
   }

   @Override
   public void blur() {
      focused = false;
   }
}
