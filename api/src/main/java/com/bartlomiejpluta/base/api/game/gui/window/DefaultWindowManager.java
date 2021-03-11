package com.bartlomiejpluta.base.api.game.gui.window;

import com.bartlomiejpluta.base.api.game.gui.base.BaseWidget;
import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.gui.base.SizeMode;
import com.bartlomiejpluta.base.api.game.screen.Screen;

import java.util.Deque;
import java.util.LinkedList;

import static java.util.Objects.requireNonNull;

public class DefaultWindowManager extends BaseWidget implements WindowManager {
   private final Deque<Window> windows = new LinkedList<>();
   private DisplayMode displayMode = DisplayMode.DISPLAY_STACK;

   public DefaultWindowManager() {
      super.setSizeMode(SizeMode.MATCH_PARENT, SizeMode.MATCH_PARENT);
   }

   @Override
   protected float getContentWidth() {
      return 0;
   }

   @Override
   protected float getContentHeight() {
      return 0;
   }

   @Override
   public void setSizeMode(SizeMode widthMode, SizeMode heightMode) {
      throw new UnsupportedOperationException("Window Manager is hardcoded to be of MATCH_PARENT mode");
   }

   @Override
   public void setWidthMode(SizeMode mode) {
      throw new UnsupportedOperationException("Window Manager is hardcoded to be of MATCH_PARENT mode");
   }

   @Override
   public void setHeightMode(SizeMode mode) {
      throw new UnsupportedOperationException("Window Manager is hardcoded to be of MATCH_PARENT mode");
   }

   @Override
   public void setDisplayMode(DisplayMode mode) {
      this.displayMode = requireNonNull(mode);
   }

   @Override
   public void open(Window window) {
      requireNonNull(window, "Window cannot be null");

      windows.addLast(window);
      window.setParent(this);
      window.onOpen(this);
   }

   @Override
   public void close() {
      if (windows.isEmpty()) {
         return;
      }

      var window = windows.removeLast();
      window.setParent(null);
      window.onClose(this);
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      switch (displayMode) {
         case DISPLAY_STACK -> {
            for (var window : windows) {
               window.draw(screen, gui);
            }
         }

         case DISPLAY_TOP -> {
            var topWindow = windows.peekFirst();
            if (topWindow != null) {
               topWindow.draw(screen, gui);
            }
         }
      }
   }
}
