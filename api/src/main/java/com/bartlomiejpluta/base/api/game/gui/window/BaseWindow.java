package com.bartlomiejpluta.base.api.game.gui.window;

import com.bartlomiejpluta.base.api.game.gui.base.BaseWidget;
import com.bartlomiejpluta.base.api.game.gui.component.Component;
import com.bartlomiejpluta.base.api.game.input.KeyEvent;

public abstract class BaseWindow extends BaseWidget implements Window {
   protected Component content;
   protected WindowPosition windowPosition;

   @Override
   public WindowPosition getWindowPosition() {
      return windowPosition;
   }

   @Override
   public void setWindowPosition(WindowPosition windowPosition) {
      this.windowPosition = windowPosition;
   }

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

   @Override
   public void handleKeyEvent(KeyEvent event) {
      content.handleKeyEvent(event);
   }
}
