package com.bartlomiejpluta.base.lib.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.screen.Screen;

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
      return component.getMarginLeft() + component.getWidth() + component.getMarginRight();
   }

   @Override
   protected float getContentHeight() {
      return component.getMarginTop() + component.getHeight() + component.getMarginBottom();
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      component.setPosition(x + component.getMarginLeft(), y + component.getMarginTop());
      component.draw(screen, gui);
   }
}
