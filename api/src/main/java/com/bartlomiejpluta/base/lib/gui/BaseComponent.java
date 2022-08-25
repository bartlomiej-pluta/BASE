package com.bartlomiejpluta.base.lib.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

public abstract class BaseComponent extends BaseWidget implements Component {
   protected final Context context;
   protected final GUI gui;
   protected final Map<String, Component> refs;
   protected boolean focused;

   protected BaseComponent(Context context, GUI gui, Map<String, Component> refs) {
      this.context = context;
      this.gui = gui;
      this.refs = refs;
   }

   @Override
   public List<Component> getChildren() {
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
