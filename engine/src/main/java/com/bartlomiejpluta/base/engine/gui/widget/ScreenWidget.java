package com.bartlomiejpluta.base.engine.gui.widget;

import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.gui.base.SizeMode;
import com.bartlomiejpluta.base.api.game.gui.base.Widget;
import com.bartlomiejpluta.base.api.game.screen.Screen;
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
   public void setX(float x) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setY(float y) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setPosition(float x, float y) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setWidth(float width) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setHeight(float height) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setSize(float width, float height) {
      throw new UnsupportedOperationException();
   }

   @Override
   public SizeMode getWidthMode() {
      return SizeMode.MATCH_PARENT;
   }

   @Override
   public void setWidthMode(SizeMode mode) {
      throw new UnsupportedOperationException();
   }

   @Override
   public SizeMode getHeightMode() {
      return SizeMode.MATCH_PARENT;
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
   public void setMargin(float top, float right, float bottom, float left) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setMargin(float top, float rightLeft, float bottom) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setMargin(float topBottom, float rightLeft) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setMargin(float all) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setMarginTop(float margin) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setMarginRight(float margin) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setMarginBottom(float margin) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setMarginLeft(float margin) {
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
   public void setPadding(float top, float right, float bottom, float left) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setPadding(float top, float rightLeft, float bottom) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setPadding(float topBottom, float rightLeft) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setPadding(float all) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setPaddingTop(float padding) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setPaddingRight(float padding) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setPaddingBottom(float padding) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setPaddingLeft(float padding) {
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
   public void draw(Screen screen, GUI gui) {
      if (root != null) {
         root.draw(screen, gui);
      }
   }
}
