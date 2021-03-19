package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.screen.Screen;

import static java.util.Objects.requireNonNull;

public abstract class ComponentWrapper extends BaseComponent {
   protected Component component;

   protected ComponentWrapper(Context context, GUI gui) {
      super(context, gui);
   }

   @Override
   public void add(Component component) {
      this.component = requireNonNull(component);
      component.setParent(this);
   }

   @Override
   protected float getContentWidth() {
      return component.getWidth();
   }

   @Override
   protected float getContentHeight() {
      return component.getHeight();
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      component.setPosition(x, y);
      component.draw(screen, gui);
   }
}
