package com.bartlomiejpluta.base.lib.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.input.Key;
import com.bartlomiejpluta.base.api.input.KeyAction;
import com.bartlomiejpluta.base.api.input.KeyEvent;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.util.math.MathUtil;
import lombok.Getter;
import lombok.NonNull;

import java.util.EnumSet;

public class VOptionChoice extends VLayout {
   private static final EnumSet<KeyAction> ACTIONS = EnumSet.of(KeyAction.PRESS, KeyAction.REPEAT);
   private int selected = 0;
   private float scroll = 0;
   private float scrollSpeed = 1f;

   @Getter
   private Component selectedComponent = null;

   public VOptionChoice(Context context, GUI gui) {
      super(context, gui);
      addEventListener(KeyEvent.TYPE, this::switchOption);
   }

   public void setScrollSpeed(@NonNull Float scrollSpeed) {
      this.scrollSpeed = MathUtil.clamp(scrollSpeed, 0f, 1f);
   }

   @Override
   public void focus() {
      super.focus();

      if (!children.isEmpty()) {
         selectedComponent = children.get(selected);
         selectedComponent.focus();
      }
   }

   @Override
   public <E extends Event> void handleEvent(E event) {
      if (selected < children.size()) {
         selectedComponent = children.get(selected);
         selectedComponent.handleEvent(event);
      }

      if (!event.isConsumed()) {
         eventHandler.handleEvent(event);
      }
   }

   private void switchOption(KeyEvent event) {
      if (children.isEmpty()) {
         return;
      }

      if (event.getKey() == Key.KEY_DOWN && ACTIONS.contains(event.getAction())) {
         blurAll();
         selected = (++selected) % children.size();
         selectedComponent = children.get(selected);
         selectedComponent.focus();
         event.consume();
      } else if (event.getKey() == Key.KEY_UP && ACTIONS.contains(event.getAction())) {
         blurAll();
         var size = children.size();
         selected = (((--selected) % size) + size) % size;
         selectedComponent = children.get(selected);
         selectedComponent.focus();
         event.consume();
      }
   }

   private void blurAll() {
      for (var child : children) {
         child.blur();
      }
   }

   @Override
   public void update(float dt) {
      super.update(dt);
      if (selectedComponent != null) {
         if (selectedComponent.getY() + selectedComponent.getHeight() > getHeight() + getY()) {
            scroll += (selectedComponent.getHeight() + selectedComponent.getY() - (getHeight() + getY())) * scrollSpeed;
         } else if (selectedComponent.getY() < getY()) {
            scroll += (selectedComponent.getY() - getY()) * scrollSpeed;
         }
      }
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      gui.clip(x, y, getWidth(), getHeight());

      offsetY = -scroll;

      super.draw(screen, gui);

      gui.resetClip();
   }
}
