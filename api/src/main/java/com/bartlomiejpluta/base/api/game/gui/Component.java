package com.bartlomiejpluta.base.api.game.gui;

public abstract class Component implements Widget {
   protected Widget parent;

   protected float absX;
   protected float absY;

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

   @Override
   public Widget getParent() {
      return parent;
   }

   public void setParent(Widget parent) {
      this.parent = parent;
   }

   @Override
   public float getAbsoluteX() {
      return absX;
   }

   @Override
   public float getAbsoluteY() {
      return absY;
   }

   @Override
   public void setAbsoluteX(float x) {
      this.absX = x;
   }

   @Override
   public void setAbsoluteY(float y) {
      this.absY = y;
   }

   @Override
   public float getWidth() {
      return width;
   }

   @Override
   public float getHeight() {
      return height;
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
}
