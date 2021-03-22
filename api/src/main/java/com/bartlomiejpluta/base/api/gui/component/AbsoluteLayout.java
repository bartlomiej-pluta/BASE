package com.bartlomiejpluta.base.api.gui.component;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.base.GUI;
import com.bartlomiejpluta.base.api.gui.base.SizeMode;
import com.bartlomiejpluta.base.api.screen.Screen;

public class AbsoluteLayout extends BaseContainer {

   public AbsoluteLayout(Context context, GUI gui) {
      super(context, gui);
      super.setSizeMode(SizeMode.ABSOLUTE, SizeMode.ABSOLUTE);
      super.setSize(0f, 0f);
   }

   @Override
   public final void setWidth(Float width) {
      throw new UnsupportedOperationException("Absolute layout does not define a size");
   }

   @Override
   public final void setHeight(Float height) {
      throw new UnsupportedOperationException("Absolute layout does not define a size");
   }

   @Override
   public final void setSizeMode(SizeMode widthMode, SizeMode heightMode) {
      throw new UnsupportedOperationException("Absolute layout does not define a size");
   }

   @Override
   public final void setWidthMode(SizeMode mode) {
      throw new UnsupportedOperationException("Absolute layout does not define a size");
   }

   @Override
   public final void setHeightMode(SizeMode mode) {
      throw new UnsupportedOperationException("Absolute layout does not define a size");
   }

   @Override
   protected final float getContentWidth() {
      return 0;
   }

   @Override
   protected final float getContentHeight() {
      return 0;
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      for (var child : children) {
         child.draw(screen, gui);
      }
   }
}
