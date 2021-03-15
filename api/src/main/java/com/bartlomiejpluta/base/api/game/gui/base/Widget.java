package com.bartlomiejpluta.base.api.game.gui.base;

import com.bartlomiejpluta.base.api.game.input.KeyEventHandler;
import com.bartlomiejpluta.base.api.game.screen.Screen;

public interface Widget extends KeyEventHandler {
   Widget getParent();

   void setParent(Widget parent);

   float getX();

   float getY();

   void setX(Float x);

   void setY(Float y);

   void setPosition(Float x, Float y);

   float getWidth();

   float getActualWidth();

   float getHeight();

   float getActualHeight();

   void setWidth(Float width);

   void setHeight(Float height);

   void setSize(Float width, Float height);

   SizeMode getWidthMode();

   void setWidthMode(SizeMode mode);

   SizeMode getHeightMode();

   void setHeightMode(SizeMode mode);

   void setSizeMode(SizeMode widthMode, SizeMode heightMode);

   void setMargin(Float top, Float right, Float bottom, Float left);

   void setMargin(Float top, Float rightLeft, Float bottom);

   void setMargin(Float topBottom, Float rightLeft);

   void setMargin(Float all);

   void setMarginTop(Float margin);

   void setMarginRight(Float margin);

   void setMarginBottom(Float margin);

   void setMarginLeft(Float margin);

   float getMarginTop();

   float getMarginRight();

   float getMarginBottom();

   float getMarginLeft();

   void setPadding(Float top, Float right, Float bottom, Float left);

   void setPadding(Float top, Float rightLeft, Float bottom);

   void setPadding(Float topBottom, Float rightLeft);

   void setPadding(Float all);

   void setPaddingTop(Float padding);

   void setPaddingRight(Float padding);

   void setPaddingBottom(Float padding);

   void setPaddingLeft(Float padding);

   float getPaddingTop();

   float getPaddingRight();

   float getPaddingBottom();

   float getPaddingLeft();

   void draw(Screen screen, GUI gui);
}
