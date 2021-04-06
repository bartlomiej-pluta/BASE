package com.bartlomiejpluta.base.engine.gui.widget;

import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.gui.SizeMode;
import com.bartlomiejpluta.base.api.gui.Widget;
import com.bartlomiejpluta.base.api.screen.Screen;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class ScreenWidget implements Widget {
   private final Screen screen;

   private Widget root;

   @Override
   public float getWidth() {
      return screen.getWidth();
   }

   @Override
   public float getActualWidth() {
      return screen.getWidth();
   }

   @Override
   public float getHeight() {
      return screen.getHeight();
   }

   @Override
   public float getActualHeight() {
      return screen.getWidth();
   }

   @Override
   public Widget getParent() {
      return null;
   }

   @Override
   public void setParent(Widget parent) {
      throw new UnsupportedOperationException();
   }

   @Override
   public float getX() {
      return 0;
   }

   @Override
   public float getY() {
      return 0;
   }

   @Override
   public void setX(Float x) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setY(Float y) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setPosition(Float x, Float y) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setWidth(Float width) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setHeight(Float height) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setSize(Float width, Float height) {
      throw new UnsupportedOperationException();
   }

   @Override
   public SizeMode getWidthMode() {
      return SizeMode.RELATIVE;
   }

   @Override
   public void setWidthMode(SizeMode mode) {
      throw new UnsupportedOperationException();
   }

   @Override
   public SizeMode getHeightMode() {
      return SizeMode.RELATIVE;
   }

   @Override
   public void setHeightMode(SizeMode mode) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setSizeMode(SizeMode widthMode, SizeMode heightMode) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setMargin(Float top, Float right, Float bottom, Float left) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setMargin(Float top, Float rightLeft, Float bottom) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setMargin(Float topBottom, Float rightLeft) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setMargin(Float all) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setMarginTop(Float margin) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setMarginRight(Float margin) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setMarginBottom(Float margin) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setMarginLeft(Float margin) {
      throw new UnsupportedOperationException();
   }

   @Override
   public float getMarginTop() {
      return 0;
   }

   @Override
   public float getMarginRight() {
      return 0;
   }

   @Override
   public float getMarginBottom() {
      return 0;
   }

   @Override
   public float getMarginLeft() {
      return 0;
   }

   @Override
   public void setPadding(Float top, Float right, Float bottom, Float left) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setPadding(Float top, Float rightLeft, Float bottom) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setPadding(Float topBottom, Float rightLeft) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setPadding(Float all) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setPaddingTop(Float padding) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setPaddingRight(Float padding) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setPaddingBottom(Float padding) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setPaddingLeft(Float padding) {
      throw new UnsupportedOperationException();
   }

   @Override
   public float getPaddingTop() {
      return 0;
   }

   @Override
   public float getPaddingRight() {
      return 0;
   }

   @Override
   public float getPaddingBottom() {
      return 0;
   }

   @Override
   public float getPaddingLeft() {
      return 0;
   }

   @Override
   public <E extends Event> void handleEvent(E event) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      if (root != null) {
         root.draw(screen, gui);
      }
   }
}
