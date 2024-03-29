package com.bartlomiejpluta.base.api.gui;

import com.bartlomiejpluta.base.internal.gc.Disposable;
import com.bartlomiejpluta.base.internal.program.Updatable;
import com.bartlomiejpluta.base.internal.render.Renderable;

public interface GUI extends Renderable, Updatable, Disposable {
   int ALIGN_LEFT = 1 << 0;
   int ALIGN_CENTER = 1 << 1;
   int ALIGN_RIGHT = 1 << 2;
   int ALIGN_TOP = 1 << 3;
   int ALIGN_MIDDLE = 1 << 4;
   int ALIGN_BOTTOM = 1 << 5;
   int ALIGN_BASELINE = 1 << 6;

   int IMAGE_GENERATE_MIPMAPS = 1 << 0;
   int IMAGE_REPEAT_X = 1 << 1;
   int IMAGE_REPEAT_Y = 1 << 2;
   int IMAGE_FLIP_Y = 1 << 3;
   int IMAGE_PREMULTIPLIED = 1 << 4;
   int IMAGE_NEAREST = 1 << 5;

   Component inflateComponent(String widgetUid);

   <T extends Component> T inflateComponent(String widgetUid, Class<T> type);

   Window inflateWindow(String widgetUid);

   <T extends Window> T inflateWindow(String widgetUid, Class<T> type);

   boolean isVisible();

   void setVisible(boolean visible);

   void show();

   void hide();

   Widget getRoot();

   void setRoot(Widget root);

   Color createColor();

   Paint createPaint();

   Image getImage(String imageUid);

   Image getImage(String imageUid, int imageFlags);

   IconSet getIconSet(String iconSetUid);

   void beginPath();

   void closePath();

   void drawRectangle(float x, float y, float width, float height);

   void drawRoundedRectangle(float x, float y, float width, float height, float radius);

   void drawCircle(float x, float y, float radius);

   void drawEllipse(float x, float y, float radiusX, float radiusY);

   void drawArc(float x, float y, float radius, float start, float end, WindingDirection direction);

   void drawArcTo(float x1, float y1, float x2, float y2, float radius);

   void drawLineTo(float x, float y);

   void drawBezierTo(float controlX1, float controlY1, float controlX2, float controlY2, float targetX, float targetY);

   void drawQuadBezierTo(float controlX, float controlY, float targetX, float targetY);

   void setLineCap(LineCap cap);

   void setLineJoin(LineCap join);

   void moveTo(float x, float y);

   void setPathWinding(WindingDirection direction);

   void setFontFace(String fontUid);

   void setFontSize(float size);

   void setFontBlur(float blur);

   void setTextAlignment(int alignment);

   void setTextLineHeight(float textLineHeight);

   void putText(float x, float y, CharSequence text, float[] outTextBounds);

   void putText(float x, float y, CharSequence text);

   void putTextBox(float x, float y, float lineWidth, CharSequence text, float[] outTextBounds);

   void putTextBox(float x, float y, float lineWidth, CharSequence text);

   void setGlobalAlpha(float alpha);

   void setFillColor(Color color);

   void setFillPaint(Paint paint);

   void fill();

   void setStrokeColor(Color color);

   void setStrokePaint(Paint paint);

   void setStrokeWidth(float width);

   void stroke();

   void boxGradient(float x, float y, float width, float height, float radius, float feather, Color inner, Color outer, Paint target);

   void linearGradient(float x, float y, float endX, float endY, Color start, Color end, Paint target);

   void radialGradient(float x, float y, float innerRadius, float outerRadius, Color start, Color end, Paint target);

   Paint imagePattern(float x, float y, float angle, float alpha, Image image, Paint target);

   Paint imagePattern(float x, float y, float width, float height, float angle, float alpha, Image image, Paint target);

   void image(float x, float y, float scaleX, float scaleY, float angle, float alpha, Image image, Paint target);

   void icon(float x, float y, float scaleX, float scaleY, float angle, float alpha, IconSet iconSet, int row, int column, Paint target);

   void clip(float x, float y, float width, float height);

   void resetClip();
}
