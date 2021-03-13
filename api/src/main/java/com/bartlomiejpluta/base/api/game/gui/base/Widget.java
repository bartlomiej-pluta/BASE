package com.bartlomiejpluta.base.api.game.gui.base;

import com.bartlomiejpluta.base.api.game.input.KeyEventHandler;
import com.bartlomiejpluta.base.api.game.screen.Screen;

public interface Widget extends KeyEventHandler {
   Widget getParent();

   void setParent(Widget parent);

   float getX();

   float getY();

   void setX(float x);

   void setY(float y);

   void setPosition(float x, float y);

   float getWidth();

   float getActualWidth();

   float getHeight();

   float getActualHeight();

   void setWidth(float width);

   void setHeight(float height);

   void setSize(float width, float height);

   SizeMode getWidthMode();

   void setWidthMode(SizeMode mode);

   SizeMode getHeightMode();

   void setHeightMode(SizeMode mode);

   void setSizeMode(SizeMode widthMode, SizeMode heightMode);

   void setMargin(float top, float right, float bottom, float left);

   void setMargin(float top, float rightLeft, float bottom);

   void setMargin(float topBottom, float rightLeft);

   void setMargin(float all);

   void setMarginTop(float margin);

   void setMarginRight(float margin);

   void setMarginBottom(float margin);

   void setMarginLeft(float margin);

   float getMarginTop();

   float getMarginRight();

   float getMarginBottom();

   float getMarginLeft();

   void setPadding(float top, float right, float bottom, float left);

   void setPadding(float top, float rightLeft, float bottom);

   void setPadding(float topBottom, float rightLeft);

   void setPadding(float all);

   void setPaddingTop(float padding);

   void setPaddingRight(float padding);

   void setPaddingBottom(float padding);

   void setPaddingLeft(float padding);

   float getPaddingTop();

   float getPaddingRight();

   float getPaddingBottom();

   float getPaddingLeft();

   void draw(Screen screen, GUI gui);
}
