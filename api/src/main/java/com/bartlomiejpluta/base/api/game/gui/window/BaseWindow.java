package com.bartlomiejpluta.base.api.game.gui.window;

import com.bartlomiejpluta.base.api.game.gui.base.BaseWidget;
import com.bartlomiejpluta.base.api.game.gui.component.Component;

public abstract class BaseWindow extends BaseWidget implements Window {
   protected Component content;

   @Override
   protected float getContentWidth() {
      return content.getMarginLeft() + content.getActualWidth() + content.getMarginRight();
   }

   @Override
   protected float getContentHeight() {
      return content.getMarginTop() + content.getActualHeight() + content.getMarginBottom();
   }

   @Override
   public void onOpen(WindowManager manager) {
      // do nothing
   }

   @Override
   public void onClose(WindowManager manager) {
      // do nothing
   }
}
