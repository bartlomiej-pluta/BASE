package com.bartlomiejpluta.base.api.gui;

public interface Component extends Widget {
   Iterable<Component> getChildren();

   void add(Component component);

   void remove(Component component);

   boolean isFocused();

   void focus();

   void blur();
}
