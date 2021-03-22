package com.bartlomiejpluta.base.api.gui.component;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.base.GUI;
import com.bartlomiejpluta.base.api.input.Key;
import com.bartlomiejpluta.base.api.input.KeyAction;
import com.bartlomiejpluta.base.api.input.KeyEvent;

import java.util.EnumSet;

public class VOptionChoice extends VScrollableLayout {
   private static final EnumSet<KeyAction> ACTIONS = EnumSet.of(KeyAction.PRESS, KeyAction.REPEAT);
   private int selected = 0;

   public VOptionChoice(Context context, GUI gui) {
      super(context, gui);
   }

   @Override
   public void focus() {
      super.focus();

      if (!children.isEmpty()) {
         children.get(selected).focus();
      }
   }

   @Override
   public void handleKeyEvent(KeyEvent event) {
      if (children.isEmpty()) {
         return;
      }

      // First we want to propagate it down tree
      children.get(selected).handleKeyEvent(event);

      // If event is still not consumed, we try to consume it right here
      if (event.isConsumed()) {
         return;
      }

      if (event.getKey() == Key.KEY_DOWN && ACTIONS.contains(event.getAction())) {
         blurAll();
         selected = (++selected) % children.size();
         children.get(selected).focus();
         scrollToSelected();
         event.consume();
      } else if (event.getKey() == Key.KEY_UP && ACTIONS.contains(event.getAction())) {
         blurAll();
         var size = children.size();
         selected = (((--selected) % size) + size) % size;
         children.get(selected).focus();
         scrollToSelected();
         event.consume();
      }
   }

   private void scrollToSelected() {
      var childrenHeight = 0.0f;

      for (int i = 0; i < selected; ++i) {
         var child = children.get(i);
         childrenHeight += child.getMarginTop() + child.getHeight() + child.getMarginBottom();
      }

      scrollTo(childrenHeight / getHeight());
   }

   private void blurAll() {
      for (var child : children) {
         child.blur();
      }
   }
}
