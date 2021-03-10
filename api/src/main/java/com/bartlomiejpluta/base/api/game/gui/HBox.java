package com.bartlomiejpluta.base.api.game.gui;

import com.bartlomiejpluta.base.api.game.screen.Screen;

public class HBox extends Container {

   @Override
   public float getWidth() {
      var childrenWidth = 0.0f;

      for (var child : children) {
         childrenWidth += child.getMarginLeft() + child.getWidth() + child.getMarginRight();
      }

      return paddingLeft + childrenWidth + paddingRight;
   }

   @Override
   public float getHeight() {
      var theHighestChild = 0.0f;

      for (var child : children) {
         var height = child.getMarginTop() + child.getHeight() + child.getMarginBottom();
         if (height > theHighestChild) {
            theHighestChild = height;
         }
      }

      return paddingTop + theHighestChild + paddingBottom;
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      var currentX = absX + paddingLeft;
      var currentY = absY + paddingTop;

      for (var child : children) {
         var childAbsX = currentX + child.getMarginLeft();
         var childAbsY = currentY + child.getMarginTop();
         child.setAbsoluteX(childAbsX);
         child.setAbsoluteY(childAbsY);

         currentX += child.getMarginLeft() + child.getWidth() + child.getMarginRight();

         child.draw(screen, gui);
      }
   }
}
