package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.gui.base.BaseWidget;
import com.bartlomiejpluta.base.api.game.gui.base.GUI;

import static java.util.Collections.emptyList;

public abstract class BaseComponent extends BaseWidget implements Component {
   protected boolean focused;
   protected final Context context;
   protected final GUI gui;

   protected BaseComponent(Context context, GUI gui) {
      this.context = context;
      this.gui = gui;
   }

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
