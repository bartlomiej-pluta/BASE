package com.bartlomiejpluta.base.api.game.gui;

import com.bartlomiejpluta.base.api.game.screen.Screen;

public class VBox extends Container {

   @Override
   public float getWidth() {
      var theWidestChild = 0.0f;

      for (var child : children) {
         var width = child.getMarginLeft() + child.getWidth() + child.getMarginRight();
         if (width > theWidestChild) {
            theWidestChild = width;
         }
      }

      return paddingLeft + theWidestChild + paddingRight;
   }

   @Override
   public float getHeight() {
      var childrenHeight = 0.0f;

      for (var child : children) {
         childrenHeight += child.getMarginTop() + child.getHeight() + child.getMarginBottom();
      }

      return paddingTop + childrenHeight + paddingBottom;
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

         currentY += child.getMarginTop() + child.getHeight() + child.getMarginBottom();

         child.draw(screen, gui);
      }
   }
}
