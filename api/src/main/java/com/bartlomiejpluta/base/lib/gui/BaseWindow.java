package com.bartlomiejpluta.base.lib.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.gui.*;
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
   public <E extends Event> void handleEvent(E event) {
      if (content != null) {
         content.handleEvent(event);
      }
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
