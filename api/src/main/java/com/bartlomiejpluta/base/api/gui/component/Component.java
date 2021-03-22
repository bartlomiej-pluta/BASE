package com.bartlomiejpluta.base.api.gui.component;

import com.bartlomiejpluta.base.api.gui.base.Widget;

public interface Component extends Widget {
   Iterable<Component> getChildren();

   void add(Component component);

   void remove(Component component);

   boolean isFocused();

   void focus();

   void blur();
}
