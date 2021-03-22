package com.bartlomiejpluta.base.lib.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.screen.Screen;

public class HLayout extends BaseContainer {
   protected float offsetX = 0.0f;
   protected float offsetY = 0.0f;

   public HLayout(Context context, GUI gui) {
      super(context, gui);
   }

   @Override
   protected float getContentWidth() {
      return sumChildrenWidth();
   }

   @Override
   protected float getContentHeight() {
      return maxChildrenHeight();
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

         currentX += child.getMarginLeft() + child.getWidth() + child.getMarginRight();

         child.draw(screen, gui);
      }
   }
}
