package com.bartlomiejpluta.base.api.game.gui.component;

public interface Container extends Component {
   void add(Component component);

   void remove(Component component);
}
