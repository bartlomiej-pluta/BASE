package com.bartlomiejpluta.base.api.game.gui.window;

import com.bartlomiejpluta.base.api.game.gui.base.BaseWidget;
import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.gui.base.SizeMode;
import com.bartlomiejpluta.base.api.game.input.KeyEvent;
import com.bartlomiejpluta.base.api.game.screen.Screen;

import java.util.Deque;
import java.util.LinkedList;

import static java.util.Objects.requireNonNull;

public final class WindowManager extends BaseWidget {
   private final Deque<Window> windows = new LinkedList<>();

   private DisplayMode displayMode;

   public WindowManager() {
      this(DisplayMode.DISPLAY_STACK);
   }

   public WindowManager(DisplayMode displayMode) {
      super.setSizeMode(SizeMode.RELATIVE, SizeMode.RELATIVE);
      super.setSize(1f, 1f);
      this.displayMode = displayMode;
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

   public void setDisplayMode(DisplayMode mode) {
      this.displayMode = requireNonNull(mode);
   }

   public void open(Window window) {
      requireNonNull(window, "Window cannot be null");

      windows.addLast(window);
      window.setParent(this);
      window.onOpen(this);
   }

   public void closeAll() {

      // Use iterator to support removal from loop inside
      for (var iterator = windows.iterator(); iterator.hasNext(); ) {
         close();
      }
   }

   public void close() {
      if (windows.isEmpty()) {
         return;
      }

      var window = windows.removeLast();
      window.setParent(null);
      window.onClose(this);
   }

   public int size() {
      return windows.size();
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      switch (displayMode) {
         case DISPLAY_STACK -> {
            for (var window : windows) {
               drawWindow(screen, window, gui);
            }
         }

         case DISPLAY_TOP -> {
            var topWindow = windows.peekLast();
            if (topWindow != null) {
               drawWindow(screen, topWindow, gui);
            }
         }
      }
   }

   @Override
   public void handleKeyEvent(KeyEvent event) {
      var topWindow = windows.peekLast();
      if (topWindow != null) {
         topWindow.handleKeyEvent(event);
      }
   }

   private void drawWindow(Screen screen, Window window, GUI gui) {
      switch (window.getWindowPosition()) {
         case TOP -> window.setPosition(
               (screen.getWidth() - window.getWidth()) / 2,
               window.getMarginTop()
         );

         case TOP_RIGHT -> window.setPosition(
               screen.getWidth() - window.getWidth() - window.getMarginRight(),
               window.getMarginTop()
         );

         case RIGHT -> window.setPosition(
               screen.getWidth() - window.getWidth() - window.getMarginRight(),
               (screen.getHeight() - window.getHeight()) / 2
         );

         case BOTTOM_RIGHT -> window.setPosition(
               screen.getWidth() - window.getWidth() - window.getMarginRight(),
               screen.getHeight() - window.getHeight() - window.getMarginBottom()
         );

         case BOTTOM -> window.setPosition(
               (screen.getWidth() - window.getWidth()) / 2,
               screen.getHeight() - window.getHeight() - window.getMarginBottom()
         );

         case BOTTOM_LEFT -> window.setPosition(
               window.getMarginLeft(),
               screen.getHeight() - window.getHeight() - window.getMarginBottom()
         );

         case LEFT -> window.setPosition(
               window.getMarginLeft(),
               (screen.getHeight() - window.getHeight()) / 2
         );

         case TOP_LEFT -> window.setPosition(
               window.getMarginLeft(),
               window.getMarginTop()
         );

         case CENTER -> window.setPosition(
               (screen.getWidth() - window.getWidth()) / 2,
               (screen.getHeight() - window.getHeight()) / 2
         );
      }

      window.draw(screen, gui);
   }
}