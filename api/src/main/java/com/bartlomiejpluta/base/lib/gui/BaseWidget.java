package com.bartlomiejpluta.base.lib.gui;

import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.event.EventType;
import com.bartlomiejpluta.base.api.gui.Attribute;
import com.bartlomiejpluta.base.api.gui.SizeMode;
import com.bartlomiejpluta.base.api.gui.Widget;
import com.bartlomiejpluta.base.lib.event.EventHandler;

import java.util.Locale;
import java.util.function.Consumer;

public abstract class BaseWidget implements Widget {
   protected Widget parent;

   protected SizeMode widthMode = SizeMode.AUTO;
   protected SizeMode heightMode = SizeMode.AUTO;

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

   protected final EventHandler eventHandler = new EventHandler();

   protected abstract float getContentWidth();

   protected abstract float getContentHeight();

   @Override
   public float getWidth() {
      return widthMode == SizeMode.RELATIVE
              ? (parent != null ? width * parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight() - marginLeft - marginRight : 0)
              : getActualWidth();
   }

   @Override
   public float getActualWidth() {
      return paddingLeft + (widthMode == SizeMode.ABSOLUTE ? width : getContentWidth()) + paddingRight;
   }

   @Override
   public float getHeight() {
      return heightMode == SizeMode.RELATIVE
              ? (parent != null ? height * parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom() - marginTop - marginBottom : 0)
              : getActualHeight();
   }

   @Override
   public float getActualHeight() {
      return paddingTop + (heightMode == SizeMode.ABSOLUTE ? height : getContentHeight()) + paddingBottom;
   }

   @Override
   public void setWidth(Float width) {
      this.width = width;
   }

   @Override
   public void setHeight(Float height) {
      this.height = height;
   }

   @Attribute("height")
   public void setHeight(String height) {
      var heightLowerCased = height.toLowerCase(Locale.ROOT);

      if (heightLowerCased.equals("auto")) {
         this.heightMode = SizeMode.AUTO;
      } else if (heightLowerCased.equals("relative")) {
         this.heightMode = SizeMode.RELATIVE;
         this.height = 1f;
      } else {
         this.heightMode = SizeMode.ABSOLUTE;
         this.height = Float.parseFloat(height);
      }
   }

   @Attribute("width")
   public void setWidth(String width) {
      var widthLowerCased = width.toLowerCase(Locale.ROOT);

      if (widthLowerCased.equals("auto")) {
         this.widthMode = SizeMode.AUTO;
      } else if (widthLowerCased.equals("relative")) {
         this.widthMode = SizeMode.RELATIVE;
         this.width = 1f;
      } else {
         this.widthMode = SizeMode.ABSOLUTE;
         this.width = Float.parseFloat(width);
      }
   }

   @Override
   public void setSize(Float width, Float height) {
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
   @Attribute("widthMode")
   public void setWidthMode(SizeMode mode) {
      this.widthMode = mode;
   }

   @Override
   public SizeMode getHeightMode() {
      return heightMode;
   }

   @Override
   @Attribute("heightMode")
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
   public void setX(Float x) {
      this.x = x;
   }

   @Override
   public void setY(Float y) {
      this.y = y;
   }

   @Override
   public void setPosition(Float x, Float y) {
      this.x = x;
      this.y = y;
   }

   @Override
   public void setMargin(Float top, Float right, Float bottom, Float left) {
      this.marginTop = top;
      this.marginRight = right;
      this.marginBottom = bottom;
      this.marginLeft = left;
   }

   @Override
   public void setMargin(Float top, Float rightLeft, Float bottom) {
      this.marginTop = top;
      this.marginRight = rightLeft;
      this.marginBottom = bottom;
      this.marginLeft = rightLeft;
   }

   @Override
   public void setMargin(Float topBottom, Float rightLeft) {
      this.marginTop = topBottom;
      this.marginRight = rightLeft;
      this.marginBottom = topBottom;
      this.marginLeft = rightLeft;
   }

   @Override
   public void setMargin(Float all) {
      this.marginTop = all;
      this.marginRight = all;
      this.marginBottom = all;
      this.marginLeft = all;
   }

   @Override
   public void setMarginTop(Float margin) {
      this.marginTop = margin;
   }

   @Override
   public void setMarginRight(Float margin) {
      this.marginRight = margin;
   }

   @Override
   public void setMarginBottom(Float margin) {
      this.marginBottom = margin;
   }

   @Override
   public void setMarginLeft(Float margin) {
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
   public void setPadding(Float top, Float right, Float bottom, Float left) {
      this.paddingTop = top;
      this.paddingRight = right;
      this.paddingBottom = bottom;
      this.paddingLeft = left;
   }

   @Override
   public void setPadding(Float top, Float rightLeft, Float bottom) {
      this.paddingTop = top;
      this.paddingRight = rightLeft;
      this.paddingBottom = bottom;
      this.paddingLeft = rightLeft;
   }

   @Override
   public void setPadding(Float topBottom, Float rightLeft) {
      this.paddingTop = topBottom;
      this.paddingRight = rightLeft;
      this.paddingBottom = topBottom;
      this.paddingLeft = rightLeft;
   }

   @Override
   public void setPadding(Float all) {
      this.paddingTop = all;
      this.paddingRight = all;
      this.paddingBottom = all;
      this.paddingLeft = all;
   }

   @Override
   public void setPaddingTop(Float padding) {
      this.paddingTop = padding;
   }

   @Override
   public void setPaddingRight(Float padding) {
      this.paddingRight = padding;
   }

   @Override
   public void setPaddingBottom(Float padding) {
      this.paddingBottom = padding;
   }

   @Override
   public void setPaddingLeft(Float padding) {
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
   public <E extends Event> void handleEvent(E event) {
      eventHandler.handleEvent(event);
   }

   @Override
   public void update(float dt) {
      // do nothing
   }

   protected <E extends Event> void addEventListener(EventType<E> type, Consumer<E> listener) {
      eventHandler.addListener(type, listener);
   }

   protected <E extends Event> void removeEventListener(EventType<E> type, Consumer<E> listener) {
      eventHandler.removeListener(type, listener);
   }
}
