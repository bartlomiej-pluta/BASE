package com.bartlomiejpluta.base.lib.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.screen.Screen;
import lombok.NonNull;

public class HGridLayout extends BaseContainer {
   protected float offsetX = 0.0f;
   protected float offsetY = 0.0f;
   protected int rows = 2;
   private float[] heights = new float[rows];

   public HGridLayout(Context context, GUI gui) {
      super(context, gui);
   }

   public void setRows(@NonNull Integer rows) {
      this.rows = rows;
      heights = new float[rows];
   }

   @Override
   protected float getContentWidth() {
      int lastId = children.size() - 1;
      var totalWidth = 0f;
      var maxWidth = 0f;
      var width = 0f;
      var i = 0;

      for (var child : children) {
         width = child.getMarginLeft() + child.getWidth() + child.getMarginRight();

         if (maxWidth < width) {
            maxWidth = width;
         }

         if (i % rows == rows - 1 || i == lastId) {
            totalWidth += maxWidth;
            maxWidth = 0f;
         }

         ++i;
      }

      return totalWidth;
   }

   @Override
   protected float getContentHeight() {
      var maxHeight = 0f;

      for (var height : heights) {
         maxHeight += height;
      }

      return maxHeight;
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      var currentX = x + paddingLeft + offsetX;
      var currentY = y + paddingTop + offsetY;

      var maxWidth = 0f;

      var i = 0;
      for (var child : children) {
         var index = i % rows;
         var height = child.getMarginTop() + child.getHeight() + child.getMarginBottom();
         if (heights[index] < height) {
            heights[index] = height;
         }
         ++i;
      }

      var row = 0;

      for (var child : children) {
         var childAbsX = currentX + child.getMarginLeft();
         var childAbsY = currentY + child.getMarginTop();
         child.setX(childAbsX);
         child.setY(childAbsY);

         if (row < rows - 1) {
            currentY += heights[row];
            var currentWidth = child.getMarginLeft() + child.getWidth() + child.getMarginRight();
            if (maxWidth < currentWidth) {
               maxWidth = currentWidth;
            }
            ++row;
         } else {
            row = 0;
            currentY = y + paddingTop + offsetY;
            currentX += maxWidth;
            maxWidth = 0f;
         }

         child.draw(screen, gui);
      }
   }
}