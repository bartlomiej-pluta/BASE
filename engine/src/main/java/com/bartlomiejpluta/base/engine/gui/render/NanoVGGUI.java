package com.bartlomiejpluta.base.engine.gui.render;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.gui.base.LineCap;
import com.bartlomiejpluta.base.api.game.gui.base.Widget;
import com.bartlomiejpluta.base.api.game.gui.base.WindingDirection;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.gui.manager.FontManager;
import com.bartlomiejpluta.base.engine.gui.widget.ScreenWidget;
import lombok.Getter;
import org.lwjgl.nanovg.NVGColor;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.system.MemoryUtil.NULL;

@Getter
public class NanoVGGUI implements GUI {
   private long context;

   private ScreenWidget screenWidget;

   private NVGColor fillColor;
   private NVGColor strokeColor;
   private final Set<String> loadedFonts = new HashSet<>();
   private final float[] boundBuffer = new float[4];

   private final FontManager fontManager;

   public NanoVGGUI(FontManager fontManager) {
      this.fontManager = fontManager;
   }

   public void init(Screen screen) {
      context = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);

      if (context == NULL) {
         throw new AppException("Could not init NanoVG");
      }

      screenWidget = new ScreenWidget(screen);

      fillColor = NVGColor.create();
      strokeColor = NVGColor.create();
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      nvgBeginFrame(context, screen.getWidth(), screen.getHeight(), 1);

      screenWidget.draw(screen, this);

      nvgEndFrame(context);
   }

   @Override
   public Widget getRoot() {
      return screenWidget.getRoot();
   }

   @Override
   public void setRoot(Widget root) {
      screenWidget.setRoot(root);
      root.setParent(screenWidget);
   }

   @Override
   public void beginPath() {
      nvgBeginPath(context);
   }

   @Override
   public void closePath() {
      nvgClosePath(context);
   }

   @Override
   public void drawRectangle(float x, float y, float width, float height) {
      nvgRect(context, x, y, width, height);
   }

   @Override
   public void drawRoundedRectangle(float x, float y, float width, float height, float radius) {
      nvgRoundedRect(context, x, y, width, height, radius);
   }

   @Override
   public void drawCircle(float x, float y, float radius) {
      nvgCircle(context, x, y, radius);
   }

   @Override
   public void drawEllipse(float x, float y, float radiusX, float radiusY) {
      nvgEllipse(context, x, y, radiusX, radiusY);
   }

   @Override
   public void drawArc(float x, float y, float radius, float start, float end, WindingDirection direction) {
      nvgArc(context, x, y, radius, start, end, direction == WindingDirection.CLOCKWISE ? NVG_CW : NVG_CCW);
   }

   @Override
   public void drawArcTo(float x1, float y1, float x2, float y2, float radius) {
      nvgArcTo(context, x1, y1, x2, y2, radius);
   }

   @Override
   public void drawLineTo(float x, float y) {
      nvgLineTo(context, x, y);
   }

   @Override
   public void drawBezierTo(float controlX1, float controlY1, float controlX2, float controlY2, float targetX, float targetY) {
      nvgBezierTo(context, controlX1, controlY1, controlX2, controlY2, targetX, targetY);
   }

   @Override
   public void drawQuadBezierTo(float controlX, float controlY, float targetX, float targetY) {
      nvgQuadTo(context, controlX, controlY, targetX, targetY);
   }

   @Override
   public void setLineCap(LineCap cap) {
      nvgLineCap(context, lineCapToNanoVGInteger(cap));
   }

   private int lineCapToNanoVGInteger(LineCap cap) {
      return switch (cap) {
         case BUTT -> NVG_BUTT;
         case ROUND -> NVG_ROUND;
         case SQUARE -> NVG_SQUARE;
         case BEVEL -> NVG_BEVEL;
         case MITER -> NVG_MITER;
      };
   }

   @Override
   public void setLineJoin(LineCap join) {
      nvgLineJoin(context, lineCapToNanoVGInteger(join));
   }

   @Override
   public void moveTo(float x, float y) {
      nvgMoveTo(context, x, y);
   }

   @Override
   public void setGlobalAlpha(float alpha) {
      nvgGlobalAlpha(context, alpha);
   }

   @Override
   public void setPathWinding(WindingDirection direction) {
      nvgPathWinding(context, direction == WindingDirection.CLOCKWISE ? NVG_CW : NVG_CCW);
   }

   @Override
   public void fillColor(float red, float green, float blue, float alpha) {
      fillColor.r(red);
      fillColor.g(green);
      fillColor.b(blue);
      fillColor.a(alpha);

      nvgFillColor(context, fillColor);
      nvgFill(context);
   }

   @Override
   public void strokeColor(float red, float green, float blue, float alpha) {
      strokeColor.r(red);
      strokeColor.g(green);
      strokeColor.b(blue);
      strokeColor.a(alpha);

      nvgStrokeColor(context, strokeColor);
      nvgStroke(context);
   }

   @Override
   public void setStrokeWidth(float width) {
      nvgStrokeWidth(context, width);
   }

   @Override
   public void putText(float x, float y, CharSequence text, float[] outTextBounds) {
      nvgText(context, x, y, text);
      nvgTextBounds(context, x, y, text, outTextBounds);
   }

   @Override
   public void putText(float x, float y, CharSequence text) {
      nvgText(context, x, y, text);
   }

   @Override
   public void putTextBox(float x, float y, float lineWidth, CharSequence text, float[] outTextBounds) {
      nvgTextBox(context, x, y, lineWidth, text);
      nvgTextBoxBounds(context, x, y, lineWidth, text, outTextBounds);
   }

   @Override
   public void putTextBox(float x, float y, float lineWidth, CharSequence text) {
      nvgTextBox(context, x, y, lineWidth, text);
   }

   @Override
   public void setFontFace(String fontUid) {
      if (!loadedFonts.contains(fontUid)) {
         var font = fontManager.loadObject(fontUid);
         nvgCreateFontMem(context, fontUid, font.getByteBuffer(), 0);
         loadedFonts.add(fontUid);
      }

      nvgFontFace(context, fontUid);
   }

   @Override
   public void setFontSize(float size) {
      nvgFontSize(context, size);
   }

   @Override
   public void setFontBlur(float blur) {
      nvgFontBlur(context, blur);
   }

   @Override
   public void setTextAlignment(int alignment) {
      nvgTextAlign(context, alignment);
   }

   @Override
   public void setTextLineHeight(float textLineHeight) {
      nvgTextLineHeight(context, textLineHeight);
   }

   @Override
   public void clip(float x, float y, float width, float height) {
      nvgScissor(context, x, y, width, height);
   }

   @Override
   public void dispose() {
      fillColor.free();
      strokeColor.free();
      nvgDelete(context);
   }
}
