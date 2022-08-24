package com.bartlomiejpluta.base.lib.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.screen.Screen;
import lombok.NonNull;

public class VGridLayout extends BaseContainer {
   protected float offsetX = 0.0f;
   protected float offsetY = 0.0f;
   private int columns = 2;
   private float[] widths = new float[columns];

   public VGridLayout(Context context, GUI gui) {
      super(context, gui);
   }

   public void setColumns(@NonNull Integer columns) {
      this.columns = columns;
      widths = new float[columns];
   }

   @Override
   protected float getContentHeight() {
      int lastId = children.size() - 1;
      var totalHeight = 0f;
      var maxHeight = 0f;
      var height = 0f;
      var i = 0;

      for (var child : children) {
         height = child.getMarginTop() + child.getHeight() + child.getMarginBottom();

         if (maxHeight < height) {
            maxHeight = height;
         }

         if (i % columns == columns - 1 || i == lastId) {
            totalHeight += maxHeight;
            maxHeight = 0f;
         }

         ++i;
      }

      return totalHeight;
   }

   @Override
   protected float getContentWidth() {
      var maxWidth = 0f;

      for (var width : widths) {
         maxWidth += width;
      }

      return maxWidth;
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      var currentX = x + paddingLeft + offsetX;
      var currentY = y + paddingTop + offsetY;

      var maxHeight = 0f;

      var i = 0;
      for (var child : children) {
         var index = i % columns;
         var width = child.getMarginLeft() + child.getWidth() + child.getMarginRight();
         if (widths[index] < width) {
            widths[index] = width;
         }
         ++i;
      }

      var column = 0;

      for (var child : children) {
         var childAbsX = currentX + child.getMarginLeft();
         var childAbsY = currentY + child.getMarginTop();
         child.setX(childAbsX);
         child.setY(childAbsY);

         if (column < columns - 1) {
            currentX += widths[column];
            var currentHeight = child.getMarginTop() + child.getHeight() + child.getMarginBottom();
            if (maxHeight < currentHeight) {
               maxHeight = currentHeight;
            }
            ++column;
         } else {
            column = 0;
            currentX = x + paddingLeft + offsetX;
            currentY += maxHeight;
            maxHeight = 0f;
         }

         child.draw(screen, gui);
      }
   }
}