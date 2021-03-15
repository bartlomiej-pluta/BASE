package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.gui.base.BaseWidget;

import static java.util.Collections.emptyList;

public abstract class BaseComponent extends BaseWidget implements Component {
   protected boolean focused;

   @Override
   public Iterable<Component> getChildren() {
      return emptyList();
   }

   @Override
   public void add(Component component) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void remove(Component component) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean isFocused() {
      return focused;
   }

   @Override
   public void focus() {
      focused = true;
   }

   @Override
   public void blur() {
      focused = false;
   }
}
