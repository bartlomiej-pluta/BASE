package com.bartlomiejpluta.base.api.gui.window;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.base.BaseWidget;
import com.bartlomiejpluta.base.api.gui.base.GUI;
import com.bartlomiejpluta.base.api.gui.component.Component;
import com.bartlomiejpluta.base.api.input.KeyEvent;
import com.bartlomiejpluta.base.api.screen.Screen;

import static java.util.Objects.requireNonNull;

public abstract class BaseWindow extends BaseWidget implements Window {
   protected Context context;
   protected GUI gui;
   protected WindowManager manager;
   protected Component content;
   protected WindowPosition windowPosition = WindowPosition.CENTER;

   protected BaseWindow(Context context, GUI gui) {
      this.context = context;
      this.gui = gui;
   }

   @Override
   public void setContent(Component component) {
      if (this.content != null) {
         this.content.setParent(null);
      }

      this.content = component;

      if (component != null) {
         component.setParent(this);
      }
   }

   @Override
   public WindowPosition getWindowPosition() {
      return windowPosition;
   }

   @Override
   public void setWindowPosition(WindowPosition windowPosition) {
      this.windowPosition = requireNonNull(windowPosition);
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
   public void draw(Screen screen, GUI gui) {
      content.setPosition(x + paddingLeft, y + paddingTop);
      content.draw(screen, gui);
   }

   @Override
   public void handleKeyEvent(KeyEvent event) {
      content.handleKeyEvent(event);
   }

   @Override
   public void onOpen(WindowManager manager) {
      this.manager = manager;
   }

   @Override
   public void onClose(WindowManager manager) {
      this.manager = null;
   }
}
