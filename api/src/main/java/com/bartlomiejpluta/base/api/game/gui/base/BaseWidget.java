package com.bartlomiejpluta.base.api.game.gui.base;

import com.bartlomiejpluta.base.api.game.input.KeyEvent;

public abstract class BaseWidget implements Widget {
   protected Widget parent;

   protected SizeMode widthMode = SizeMode.NORMAL;
   protected SizeMode heightMode = SizeMode.NORMAL;

   protected float x;
   protected float y;

   protected float width;
   protected float height;

   protected float marginTop;
   protected float marginRight;
   protected float marginBottom;
   protected float marginLeft;

   protected float paddingTop;
   protected float paddingRight;
   protected float paddingBottom;
   protected float paddingLeft;

   protected abstract float getContentWidth();

   protected abstract float getContentHeight();

   @Override
   public float getWidth() {
      return widthMode == SizeMode.MATCH_PARENT
            ? (parent != null ? parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight() - marginLeft - marginRight : 0)
            : getActualWidth();
   }

   @Override
   public float getActualWidth() {
      return paddingLeft + (widthMode == SizeMode.NORMAL ? width : getContentWidth()) + paddingRight;
   }

   @Override
   public float getHeight() {
      return heightMode == SizeMode.MATCH_PARENT
            ? (parent != null ? parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom() - marginTop - marginBottom : 0)
            : getActualHeight();
   }

   @Override
   public float getActualHeight() {
      return paddingTop + (heightMode == SizeMode.NORMAL ? height : getContentHeight()) + paddingBottom;
   }

   @Override
   public void setWidth(float width) {
      this.width = width;
   }

   @Override
   public void setHeight(float height) {
      this.height = height;
   }

   @Override
   public void setSize(float width, float height) {
      this.width = width;
      this.height = height;
   }

   @Override
   public void setSizeMode(SizeMode widthMode, SizeMode heightMode) {
      this.widthMode = widthMode;
      this.heightMode = heightMode;
   }

   @Override
   public SizeMode getWidthMode() {
      return widthMode;
   }

   @Override
   public void setWidthMode(SizeMode mode) {
      this.widthMode = mode;
   }

   @Override
   public SizeMode getHeightMode() {
      return heightMode;
   }

   @Override
   public void setHeightMode(SizeMode mode) {
      this.heightMode = mode;
   }

   @Override
   public Widget getParent() {
      return parent;
   }

   @Override
   public void setParent(Widget parent) {
      this.parent = parent;
   }

   @Override
   public float getX() {
      return x;
   }

   @Override
   public float getY() {
      return y;
   }

   @Override
   public void setX(float x) {
      this.x = x;
   }

   @Override
   public void setY(float y) {
      this.y = y;
   }

   @Override
   public void setPosition(float x, float y) {
      this.x = x;
      this.y = y;
   }

   @Override
   public void setMargin(float top, float right, float bottom, float left) {
      this.marginTop = top;
      this.marginRight = right;
      this.marginBottom = bottom;
      this.marginLeft = left;
   }

   @Override
   public void setMargin(float top, float rightLeft, float bottom) {
      this.marginTop = top;
      this.marginRight = rightLeft;
      this.marginBottom = bottom;
      this.marginLeft = rightLeft;
   }

   @Override
   public void setMargin(float topBottom, float rightLeft) {
      this.marginTop = topBottom;
      this.marginRight = rightLeft;
      this.marginBottom = topBottom;
      this.marginLeft = rightLeft;
   }

   @Override
   public void setMargin(float all) {
      this.marginTop = all;
      this.marginRight = all;
      this.marginBottom = all;
      this.marginLeft = all;
   }

   @Override
   public void setMarginTop(float margin) {
      this.marginTop = margin;
   }

   @Override
   public void setMarginRight(float margin) {
      this.marginRight = margin;
   }

   @Override
   public void setMarginBottom(float margin) {
      this.marginBottom = margin;
   }

   @Override
   public void setMarginLeft(float margin) {
      this.marginLeft = margin;
   }

   @Override
   public float getMarginTop() {
      return marginTop;
   }

   @Override
   public float getMarginRight() {
      return marginRight;
   }

   @Override
   public float getMarginBottom() {
      return marginBottom;
   }

   @Override
   public float getMarginLeft() {
      return marginLeft;
   }

   @Override
   public void setPadding(float top, float right, float bottom, float left) {
      this.paddingTop = top;
      this.paddingRight = right;
      this.paddingBottom = bottom;
      this.paddingLeft = left;
   }

   @Override
   public void setPadding(float top, float rightLeft, float bottom) {
      this.paddingTop = top;
      this.paddingRight = rightLeft;
      this.paddingBottom = bottom;
      this.paddingLeft = rightLeft;
   }

   @Override
   public void setPadding(float topBottom, float rightLeft) {
      this.paddingTop = topBottom;
      this.paddingRight = rightLeft;
      this.paddingBottom = topBottom;
      this.paddingLeft = rightLeft;
   }

   @Override
   public void setPadding(float all) {
      this.paddingTop = all;
      this.paddingRight = all;
      this.paddingBottom = all;
      this.paddingLeft = all;
   }

   @Override
   public void setPaddingTop(float padding) {
      this.paddingTop = padding;
   }

   @Override
   public void setPaddingRight(float padding) {
      this.paddingRight = padding;
   }

   @Override
   public void setPaddingBottom(float padding) {
      this.paddingBottom = padding;
   }

   @Override
   public void setPaddingLeft(float padding) {
      this.paddingLeft = padding;
   }

   @Override
   public float getPaddingTop() {
      return paddingTop;
   }

   @Override
   public float getPaddingRight() {
      return paddingRight;
   }

   @Override
   public float getPaddingBottom() {
      return paddingBottom;
   }

   @Override
   public float getPaddingLeft() {
      return paddingLeft;
   }

   @Override
   public void handleKeyEvent(KeyEvent event) {
      // Designed to be overridden if needed so
   }
}
