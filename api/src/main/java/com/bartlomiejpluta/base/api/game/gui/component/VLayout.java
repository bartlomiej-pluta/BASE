package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.screen.Screen;

public class VLayout extends BaseContainer {

   @Override
   public float getWidth() {
      return paddingLeft + maxChildrenWidth() + paddingRight;
   }

   @Override
   public float getHeight() {
      return paddingTop + sumChildrenHeight() + paddingBottom;
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      var currentX = x + paddingLeft;
      var currentY = y + paddingTop;

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
