package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.screen.Screen;

public class HLayout extends BaseContainer {

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
      var currentX = x + paddingLeft;
      var currentY = y + paddingTop;

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
