package com.bartlomiejpluta.base.api.game.gui;

import com.bartlomiejpluta.base.api.internal.gc.Disposable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;

public interface GUI extends Renderable, Disposable {
   Widget getRoot();

   void setRoot(Widget root);

   void beginPath();

   void drawRectangle(float x, float y, float w, float h);

   void fillColor(Color color);

   void setFontFace(String fontUid);

   void setFontSize(float size);

   void putText(float x, float y, CharSequence text);

   Color createColor(float red, float green, float blue, float alpha);
}
