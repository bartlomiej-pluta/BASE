package com.bartlomiejpluta.base.api.game.gui;

import java.util.LinkedList;
import java.util.List;

public abstract class Container extends Component {
   protected final List<Component> children = new LinkedList<>();

   public void addChild(Component component) {
      this.children.add(component);
      component.setParent(this);
   }
}
