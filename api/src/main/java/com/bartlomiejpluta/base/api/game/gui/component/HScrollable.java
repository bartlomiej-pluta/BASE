package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.gui.base.SizeMode;
import com.bartlomiejpluta.base.api.game.input.Key;
import com.bartlomiejpluta.base.api.game.input.KeyAction;
import com.bartlomiejpluta.base.api.game.input.KeyEvent;
import com.bartlomiejpluta.base.api.game.screen.Screen;

import static com.bartlomiejpluta.base.api.util.math.MathUtil.clamp;
import static java.lang.Math.*;

public class HScrollable extends SingleChildContainer {
   private float scroll = 0.0f;
   private float scrollStep = 0.25f;
   private float scrollSpeed = 0.1f;

   private float scrollTarget = 0.0f;
   private float scrollPosition = 0.0f;

   public HScrollable(Context context, GUI gui) {
      super(context, gui);
   }

   public float getScroll() {
      return scroll;
   }

   public void setScroll(Float scroll) {
      this.scroll = clamp(scroll, 0, 1);
   }

   public float getScrollStep() {
      return scrollStep;
   }

   public void setScrollStep(Float scrollStep) {
      this.scrollStep = clamp(scrollStep, 0, 1);
   }

   public float getScrollSpeed() {
      return scrollSpeed;
   }

   public void setScrollSpeed(Float scrollSpeed) {
      this.scrollSpeed = clamp(scrollSpeed, 0, 1);
   }

   @Override
   public void remove(Component component) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setSizeMode(SizeMode widthMode, SizeMode heightMode) {
      if (widthMode == SizeMode.AUTO || heightMode == SizeMode.AUTO) {
         throw new IllegalStateException("Scrollable component size mode must be other than AUTO");
      }

      super.setSizeMode(widthMode, heightMode);
   }

   @Override
   public void setWidthMode(SizeMode mode) {
      if (mode == SizeMode.AUTO) {
         throw new IllegalStateException("Scrollable component size mode must be other than AUTO");
      }

      super.setWidthMode(mode);
   }

   @Override
   public void setHeightMode(SizeMode mode) {
      if (mode == SizeMode.AUTO) {
         throw new IllegalStateException("Scrollable component size mode must be other than AUTO");
      }

      super.setHeightMode(mode);
   }

   @Override
   protected float getContentWidth() {
      return child.getMarginLeft() + child.getActualWidth() + child.getMarginRight();
   }

   @Override
   protected float getContentHeight() {
      return child.getMarginTop() + child.getActualHeight() + child.getMarginBottom();
   }

   @Override
   public void handleKeyEvent(KeyEvent event) {
      super.handleKeyEvent(event);

      if (event.isConsumed()) {
         return;
      }

      if (event.getKey() == Key.KEY_RIGHT && (event.getAction() == KeyAction.PRESS || event.getAction() == KeyAction.REPEAT)) {
         scroll = min(scroll + scrollStep, 1);
         scrollTarget = scroll * max(getContentWidth() - getWidth(), 0);
         event.consume();
      }

      if (event.getKey() == Key.KEY_LEFT && (event.getAction() == KeyAction.PRESS || event.getAction() == KeyAction.REPEAT)) {
         scroll = max(scroll - scrollStep, 0);
         scrollTarget = scroll * max(getContentWidth() - getWidth(), 0);
         event.consume();
      }
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      var remainingDistance = scrollTarget - scrollPosition;
      if (abs(remainingDistance) > scrollSpeed) {
         scrollPosition += scrollSpeed * remainingDistance;
      }

      gui.clip(x, y, getWidth(), getHeight());
      child.setPosition(x + paddingLeft + child.getMarginLeft() - scrollPosition, y + paddingTop + child.getMarginTop());
      child.draw(screen, gui);
      gui.resetClip();
   }
}
