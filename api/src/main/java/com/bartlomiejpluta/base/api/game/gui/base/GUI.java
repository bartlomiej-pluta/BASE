package com.bartlomiejpluta.base.api.game.gui.base;

import com.bartlomiejpluta.base.api.internal.gc.Disposable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;

public interface GUI extends Renderable, Disposable {
   int ALIGN_LEFT = 1 << 0;
   int ALIGN_CENTER = 1 << 1;
   int ALIGN_RIGHT = 1 << 2;
   int ALIGN_TOP = 1 << 3;
   int ALIGN_MIDDLE = 1 << 4;
   int ALIGN_BOTTOM = 1 << 5;
   int ALIGN_BASELINE = 1 << 6;

   Widget getRoot();

   void setRoot(Widget root);

   void beginPath();

   void drawRectangle(float x, float y, float w, float h);

   void fillColor(float red, float green, float blue, float alpha);

   void strokeColor(float red, float green, float blue, float alpha);

   void setFontFace(String fontUid);

   void setFontSize(float size);

   void setTextAlignment(int alignment);

   void putText(float x, float y, CharSequence text, float[] outTextBounds);

   void putText(float x, float y, CharSequence text);

   void putTextBox(float x, float y, float lineWidth, CharSequence text, float[] outTextBounds);

   void putTextBox(float x, float y, float lineWidth, CharSequence text);
}
