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

public class HGridOptionChoice extends HGridLayout {
   private static final EnumSet<KeyAction> ACTIONS = EnumSet.of(KeyAction.PRESS, KeyAction.REPEAT);
   private int selectedRow = 0;
   private int selectedColumn = 0;
   private float scrollX = 0;
   private float scrollY = 0;
   private float scrollSpeedX = 1f;
   private float scrollSpeedY = 1f;

   @Getter
   private Component selectedComponent = null;

   public HGridOptionChoice(Context context, GUI gui) {
      super(context, gui);
      addEventListener(KeyEvent.TYPE, this::switchOption);
   }

   public void setScrollSpeed(@NonNull Float scrollSpeed) {
      var coerced = MathUtil.clamp(scrollSpeed, 0f, 1f);
      this.scrollSpeedX = coerced;
      this.scrollSpeedY = coerced;
   }

   public void setScrollSpeedX(@NonNull Float scrollSpeedX) {
      this.scrollSpeedX = MathUtil.clamp(scrollSpeedX, 0f, 1f);
   }

   public void setScrollSpeedY(@NonNull Float scrollSpeedY) {
      this.scrollSpeedY = MathUtil.clamp(scrollSpeedY, 0f, 1f);
   }

   @Override
   public void focus() {
      super.focus();

      if (!children.isEmpty()) {
         selectedComponent = children.get(rows * selectedColumn + selectedRow);
         selectedComponent.focus();
      }
   }

   @Override
   public <E extends Event> void handleEvent(E event) {
      var index = rows * selectedColumn + selectedRow;
      if (index < children.size()) {
         selectedComponent = children.get(index);
         selectedComponent.handleEvent(event);
      }

      if (!event.isConsumed()) {
         eventHandler.handleEvent(event);
      }
   }

   private void blurAll() {
      for (var child : children) {
         child.blur();
      }
   }

   private void switchOption(KeyEvent event) {
      if (children.isEmpty()) {
         return;
      }

      if (event.getKey() == Key.KEY_DOWN && ACTIONS.contains(event.getAction())) {
         blurAll();

         int size = 0;
         for (int i = 0; i < children.size(); ++i) {
            if (i / rows == selectedColumn) ++size;
         }

         if (size == 0) return;

         selectedRow = (++selectedRow) % size;
         selectedComponent = children.get(rows * selectedColumn + selectedRow);
         selectedComponent.focus();
         event.consume();
      } else if (event.getKey() == Key.KEY_UP && ACTIONS.contains(event.getAction())) {
         blurAll();

         int size = 0;
         for (int i = 0; i < children.size(); ++i) {
            if (i / rows == selectedColumn) ++size;
         }

         if (size == 0) return;

         selectedRow = ((--selectedRow) + size) % size;
         selectedComponent = children.get(rows * selectedColumn + selectedRow);
         selectedComponent.focus();
         event.consume();
      } else if (event.getKey() == Key.KEY_RIGHT && ACTIONS.contains(event.getAction())) {
         blurAll();

         int size = 0;
         for (int i = 0; i < children.size(); ++i) {
            if (i % rows == selectedRow) ++size;
         }

         if (size == 0) return;

         selectedColumn = (++selectedColumn) % size;
         selectedComponent = children.get(rows * selectedColumn + selectedRow);
         selectedComponent.focus();
         event.consume();
      } else if (event.getKey() == Key.KEY_LEFT && ACTIONS.contains(event.getAction())) {
         blurAll();

         int size = 0;
         for (int i = 0; i < children.size(); ++i) {
            if (i % rows == selectedRow) ++size;
         }

         if (size == 0) return;

         selectedColumn = ((--selectedColumn) + size) % size;
         selectedComponent = children.get(rows * selectedColumn + selectedRow);
         selectedComponent.focus();
         event.consume();
      }
   }

   @Override
   public void update(float dt) {
      super.update(dt);

      if (selectedComponent != null) {
         if (selectedComponent.getX() + selectedComponent.getWidth() > getWidth() + getX()) {
            scrollX += (selectedComponent.getWidth() + selectedComponent.getX() - (getWidth() + getX())) * scrollSpeedX;
         } else if (selectedComponent.getX() < getX()) {
            scrollX += (selectedComponent.getX() - getX()) * scrollSpeedX;
         }
      }

      if (selectedComponent != null) {
         if (selectedComponent.getY() + selectedComponent.getHeight() > getHeight() + getY()) {
            scrollY += (selectedComponent.getHeight() + selectedComponent.getY() - (getHeight() + getY())) * scrollSpeedY;
         } else if (selectedComponent.getY() < getY()) {
            scrollY += (selectedComponent.getY() - getY()) * scrollSpeedY;
         }
      }
   }


   @Override
   public void draw(Screen screen, GUI gui) {
      gui.clip(x, y, getWidth(), getHeight());

      offsetX = -scrollX;
      offsetY = -scrollY;

      super.draw(screen, gui);

      gui.resetClip();
   }
}