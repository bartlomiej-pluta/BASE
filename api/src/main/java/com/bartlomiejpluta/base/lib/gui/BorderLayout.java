package com.bartlomiejpluta.base.lib.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.Attribute;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.gui.SizeMode;
import com.bartlomiejpluta.base.api.screen.Screen;

import java.util.Map;

public class BorderLayout extends BaseContainer {

   public BorderLayout(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
      super.setSizeMode(SizeMode.RELATIVE, SizeMode.RELATIVE);
      super.setSize(1f, 1f);
   }

   @Override
   public void add(Component component) {
      if (!(component instanceof Slot)) {
         throw new IllegalStateException("Border layout directly accepts only Slot children");
      }

      super.add(component);
   }

   @Override
   @Attribute("width")
   public void setWidth(String width) {
      super.setWidth(width);

      if (widthMode == SizeMode.AUTO) {
         throw new IllegalStateException("Border layout does not support AUTO sizing mode");
      }
   }

   @Override
   @Attribute("height")
   public void setHeight(String height) {
      super.setHeight(height);

      if (heightMode == SizeMode.AUTO) {
         throw new IllegalStateException("Border layout does not support AUTO sizing mode");
      }
   }

   @Override
   public final void setSizeMode(SizeMode widthMode, SizeMode heightMode) {
      if (widthMode == SizeMode.AUTO || heightMode == SizeMode.AUTO) {
         throw new IllegalStateException("Border layout does not support AUTO sizing mode");
      }

      super.setSizeMode(widthMode, heightMode);
   }

   @Override
   public final void setWidthMode(SizeMode mode) {
      if (mode == SizeMode.AUTO) {
         throw new IllegalStateException("Border layout does not support AUTO sizing mode");
      }

      super.setWidthMode(mode);
   }

   @Override
   public final void setHeightMode(SizeMode mode) {
      if (mode == SizeMode.AUTO) {
         throw new IllegalStateException("Border layout does not support AUTO sizing mode");
      }

      super.setHeightMode(mode);
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
         ((Slot) child).setPosition(this);
         child.draw(screen, gui);
      }
   }

   public static abstract class Slot extends ComponentWrapper {
      protected Slot(Context context, GUI gui, Map<String, Component> refs) {
         super(context, gui, refs);
      }

      protected abstract void setPosition(BorderLayout layout);
   }

   public static class Top extends Slot {

      public Top(Context context, GUI gui, Map<String, Component> refs) {
         super(context, gui, refs);
      }

      @Override
      protected void setPosition(BorderLayout layout) {
         setPosition((layout.getWidth() - getWidth()) / 2, layout.getMarginTop() + layout.getPaddingTop());
      }
   }

   public static class TopRight extends Slot {

      public TopRight(Context context, GUI gui, Map<String, Component> refs) {
         super(context, gui, refs);
      }

      @Override
      protected void setPosition(BorderLayout layout) {
         setPosition(layout.getWidth() - getWidth() - getMarginRight() - layout.getPaddingRight(), getMarginTop() + layout.getPaddingTop());
      }
   }

   public static class Right extends Slot {

      public Right(Context context, GUI gui, Map<String, Component> refs) {
         super(context, gui, refs);
      }

      @Override
      protected void setPosition(BorderLayout layout) {
         setPosition(layout.getWidth() - getWidth() - getMarginRight() - layout.getPaddingRight(), (layout.getHeight() - getHeight()) / 2);
      }
   }

   public static class BottomRight extends Slot {

      public BottomRight(Context context, GUI gui, Map<String, Component> refs) {
         super(context, gui, refs);
      }

      @Override
      protected void setPosition(BorderLayout layout) {
         setPosition(layout.getWidth() - getWidth() - getMarginRight() - layout.getPaddingRight(), layout.getHeight() - getHeight() - getMarginBottom() - layout.getPaddingBottom());
      }
   }

   public static class Bottom extends Slot {

      public Bottom(Context context, GUI gui, Map<String, Component> refs) {
         super(context, gui, refs);
      }

      @Override
      protected void setPosition(BorderLayout layout) {
         setPosition((layout.getWidth() - getWidth()) / 2, layout.getHeight() - getHeight() - getMarginBottom() - layout.getPaddingBottom());
      }
   }

   public static class BottomLeft extends Slot {

      public BottomLeft(Context context, GUI gui, Map<String, Component> refs) {
         super(context, gui, refs);
      }

      @Override
      protected void setPosition(BorderLayout layout) {
         setPosition(getMarginLeft() + layout.getPaddingLeft(), layout.getHeight() - getHeight() - getMarginBottom() - layout.getPaddingBottom());
      }
   }

   public static class Left extends Slot {

      public Left(Context context, GUI gui, Map<String, Component> refs) {
         super(context, gui, refs);
      }

      @Override
      protected void setPosition(BorderLayout layout) {
         setPosition(getMarginLeft() + layout.getPaddingLeft(), (layout.getHeight() - getHeight()) / 2);
      }
   }

   public static class TopLeft extends Slot {

      public TopLeft(Context context, GUI gui, Map<String, Component> refs) {
         super(context, gui, refs);
      }

      @Override
      protected void setPosition(BorderLayout layout) {
         setPosition(getMarginLeft() + layout.getPaddingLeft(), getMarginTop() + layout.getPaddingTop());
      }
   }

   public static class Center extends Slot {

      public Center(Context context, GUI gui, Map<String, Component> refs) {
         super(context, gui, refs);
      }

      @Override
      protected void setPosition(BorderLayout layout) {
         setPosition((layout.getWidth() - getWidth()) / 2, (layout.getHeight() - getHeight()) / 2);
      }
   }
}
