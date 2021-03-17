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

public class VScrollableLayout extends VLayout {
   private float scroll = 0.0f;
   private float scrollStep = 0.25f;
   private float scrollSpeed = 0.1f;

   public VScrollableLayout(Context context, GUI gui) {
      super(context, gui);
   }

   public float getActualScroll() {
      return getContentHeight() * -offsetY;
   }

   public int getCurrentPage() {
      return (int) floor(scroll / scrollStep);
   }

   public int getPages() {
      return (int) ceil(1 / scrollStep);
   }

   public float getTargetScroll() {
      return scroll;
   }

   public void scrollTo(Float scroll) {
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
   public void handleKeyEvent(KeyEvent event) {
      super.handleKeyEvent(event);

      if (event.isConsumed()) {
         return;
      }

      if (event.getKey() == Key.KEY_DOWN && (event.getAction() == KeyAction.PRESS || event.getAction() == KeyAction.REPEAT)) {
         scroll = min(scroll + scrollStep, 1);
         event.consume();
      }

      if (event.getKey() == Key.KEY_UP && (event.getAction() == KeyAction.PRESS || event.getAction() == KeyAction.REPEAT)) {
         scroll = max(scroll - scrollStep, 0);
         event.consume();
      }
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      var remainingDistance = -scroll * max(getContentHeight() - getHeight(), 0) - offsetY;
      if (abs(remainingDistance) > scrollSpeed) {
         offsetY += scrollSpeed * remainingDistance;
      }

      gui.clip(x, y, getWidth(), getHeight());
      super.draw(screen, gui);
      gui.resetClip();
   }
}
