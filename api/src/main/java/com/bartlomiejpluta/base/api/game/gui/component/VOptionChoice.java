package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.input.Key;
import com.bartlomiejpluta.base.api.game.input.KeyAction;
import com.bartlomiejpluta.base.api.game.input.KeyEvent;

import java.util.EnumSet;

public class VOptionChoice extends VLayout {
   private static final EnumSet<KeyAction> ACTIONS = EnumSet.of(KeyAction.PRESS, KeyAction.REPEAT);
   private int selected = 0;

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

      if (event.getKey() == Key.KEY_DOWN) {
         if (ACTIONS.contains(event.getAction())) {
            blurAll();
            selected = (++selected) % children.size();
            children.get(selected).focus();
            event.consume();
         }
      } else if (event.getKey() == Key.KEY_UP) {
         if (ACTIONS.contains(event.getAction())) {
            blurAll();
            var size = children.size();
            selected = (((--selected) % size) + size) % size;
            children.get(selected).focus();
            event.consume();
         }
      } else {

      }
   }

   private void blurAll() {
      for (var child : children) {
         child.blur();
      }
   }
}