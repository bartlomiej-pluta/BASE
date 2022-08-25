package com.bartlomiejpluta.base.api.gui;

import java.util.List;

public interface Component extends Widget {
   List<Component> getChildren();

   void add(Component component);

   void remove(Component component);

   boolean isFocused();

   void focus();

   void blur();
}
