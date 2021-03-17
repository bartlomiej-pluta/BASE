package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.screen.Screen;

public class VLayout extends BaseContainer {
   protected float offsetX = 0.0f;
   protected float offsetY = 0.0f;

   public VLayout(Context context, GUI gui) {
      super(context, gui);
   }

   @Override
   protected float getContentWidth() {
      return maxChildrenWidth();
   }

   @Override
   protected float getContentHeight() {
      return sumChildrenHeight();
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      var currentX = x + paddingLeft + offsetX;
      var currentY = y + paddingTop + offsetY;

      for (var child : children) {
         var childAbsX = currentX + child.getMarginLeft();
         var childAbsY = currentY + child.getMarginTop();
         child.setX(childAbsX);
         child.setY(childAbsY);

         currentY += child.getMarginTop() + child.getHeight() + child.getMarginBottom();

         child.draw(screen, gui);
      }
   }
}
