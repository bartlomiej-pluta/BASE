package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.gui.base.GUI;

public abstract class SingleChildContainer extends BaseContainer {
   protected Component child;

   protected SingleChildContainer(Context context, GUI gui) {
      super(context, gui);
   }

   @Override
   public void add(Component component) {
      children.clear();
      super.add(component);
      this.child = component;
   }

   @Override
   public void remove(Component component) {
      super.remove(component);
      this.child = null;
   }
}
