package com.bartlomiejpluta.base.api.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.input.Input;
import com.bartlomiejpluta.base.api.input.KeyEvent;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.lib.gui.BaseWidget;
import lombok.NonNull;
import lombok.Setter;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

public final class WindowManager extends BaseWidget {
   private final Input input;
   private final Deque<Window> windows = new LinkedList<>();

   @NonNull
   @Setter
   private DisplayMode displayMode;

   @NonNull
   @Setter
   private UpdateMode updateMode;

   public WindowManager(Context context) {
      this(context, DisplayMode.DISPLAY_STACK, UpdateMode.UPDATE_ALL);
   }

   public WindowManager(@NonNull Context context, @NonNull DisplayMode displayMode, @NonNull UpdateMode updateMode) {
      this.input = context.getInput();

      super.setSizeMode(SizeMode.RELATIVE, SizeMode.RELATIVE);
      super.setSize(1f, 1f);
      this.displayMode = displayMode;
      this.updateMode = updateMode;
   }

   @Override
   protected final float getContentWidth() {
      return 0;
   }

   @Override
   protected final float getContentHeight() {
      return 0;
   }

   @Override
   public final void setSizeMode(SizeMode widthMode, SizeMode heightMode) {
      throw new UnsupportedOperationException("Window Manager is hardcoded to be of MATCH_PARENT mode");
   }

   @Override
   public final void setWidthMode(SizeMode mode) {
      throw new UnsupportedOperationException("Window Manager is hardcoded to be of MATCH_PARENT mode");
   }

   @Override
   public final void setHeightMode(SizeMode mode) {
      throw new UnsupportedOperationException("Window Manager is hardcoded to be of MATCH_PARENT mode");
   }

   @Override
   @Attribute("width")
   public void setWidth(String width) {
      super.setWidth(width);

      if (widthMode == SizeMode.AUTO) {
         throw new IllegalStateException("Border layout does not support AUTO sizing mode");
      }
   }

   @Override
   @Attribute("height")
   public void setHeight(String height) {
      super.setHeight(height);

      if (heightMode == SizeMode.AUTO) {
         throw new IllegalStateException("Border layout does not support AUTO sizing mode");
      }
   }

   public CompletableFuture<Window> open(@NonNull Window window, Object... args) {
      if (windows.isEmpty()) {
         input.addKeyEventHandler(this::forwardKeyEventToTopWindow);
      }

      windows.addLast(window);
      window.setParent(this);
      window.onOpen(this, args != null ? args : new Object[]{});

      return window.getFuture();
   }

   private void forwardKeyEventToTopWindow(KeyEvent event) {
      var topWindow = windows.peekLast();
      if (topWindow != null) {
         topWindow.handleEvent(event);
      }
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

      if (windows.isEmpty()) {
         input.removeKeyEventHandler(this::forwardKeyEventToTopWindow);
      }

      window.getFuture().complete(window);
   }

   public int size() {
      return windows.size();
   }

   public boolean isEmpty() {
      return windows.isEmpty();
   }

   public Window top() {
      return windows.peekLast();
   }

   @Override
   public void update(float dt) {
      switch (updateMode) {
         case UPDATE_ALL -> {
            for (var window : windows) {
               window.update(dt);
            }
         }

         case UPDATE_TOP -> {
            var topWindow = windows.peekLast();
            if (topWindow != null) {
               topWindow.update(dt);
            }
         }
      }
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