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
import lombok.NonNull;

import java.util.EnumSet;

public class HOptionChoice extends HLayout {
   private static final EnumSet<KeyAction> ACTIONS = EnumSet.of(KeyAction.PRESS, KeyAction.REPEAT);
   private int selected = 0;
   private float scroll = 0;
   private Component component = null;
   private float scrollSpeed = 1f;

   public HOptionChoice(Context context, GUI gui) {
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
         component = children.get(selected);
         component.focus();
      }
   }

   @Override
   public <E extends Event> void handleEvent(E event) {
      if (selected < children.size()) {
         component = children.get(selected);
         component.handleEvent(event);
      }

      if (!event.isConsumed()) {
         eventHandler.handleEvent(event);
      }
   }

   private void switchOption(KeyEvent event) {
      if (children.isEmpty()) {
         return;
      }

      if (event.getKey() == Key.KEY_RIGHT && ACTIONS.contains(event.getAction())) {
         blurAll();
         selected = (++selected) % children.size();
         component = children.get(selected);
         component.focus();
         event.consume();
      } else if (event.getKey() == Key.KEY_LEFT && ACTIONS.contains(event.getAction())) {
         blurAll();
         var size = children.size();
         selected = (((--selected) % size) + size) % size;
         component = children.get(selected);
         component.focus();
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
      if (component != null) {
         if (component.getX() + component.getWidth() > getWidth() + getX()) {
            scroll += (component.getWidth() + component.getX() - (getWidth() + getX())) * scrollSpeed;
         } else if (component.getX() < getX()) {
            scroll += (component.getX() - getX()) * scrollSpeed;
         }
      }
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      gui.clip(x, y, getWidth(), getHeight());

      offsetX = -scroll;

      super.draw(screen, gui);

      gui.resetClip();
   }
}
