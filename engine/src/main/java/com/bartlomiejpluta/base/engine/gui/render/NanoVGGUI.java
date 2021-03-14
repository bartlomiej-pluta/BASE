package com.bartlomiejpluta.base.engine.gui.render;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.gui.base.*;
import com.bartlomiejpluta.base.api.game.input.KeyEvent;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.gui.manager.FontManager;
import com.bartlomiejpluta.base.engine.gui.widget.ScreenWidget;
import com.bartlomiejpluta.base.engine.world.image.manager.ImageManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.nanovg.NVGColor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.system.MemoryUtil.NULL;

@Slf4j
@Getter
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NanoVGGUI implements GUI {
   private final FontManager fontManager;
   private final ImageManager imageManager;

   private long context;
   private ScreenWidget screenWidget;

   private final List<NanoVGColor> colors = new LinkedList<>();
   private final Set<String> loadedFonts = new HashSet<>();
   private final Map<String, Integer> loadedImages = new HashMap<>();

   public void init(Screen screen) {
      context = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);

      if (context == NULL) {
         throw new AppException("Could not init NanoVG");
      }

      screenWidget = new ScreenWidget(screen);
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
   public Color createColor() {
      log.info("Creating new GUI color");
      var color = new NanoVGColor(NVGColor.create());
      colors.add(color);
      return color;
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
   public void setFillColor(Color color) {
      nvgFillColor(context, ((NanoVGColor) color).getColor());
   }

   @Override
   public void fill() {
      nvgFill(context);
   }

   @Override
   public void setStrokeColor(Color color) {
      nvgStrokeColor(context, ((NanoVGColor) color).getColor());
   }

   @Override
   public void stroke() {
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
         var fontBuffer = fontManager.loadObjectByteBuffer(fontUid);
         nvgCreateFontMem(context, fontUid, fontBuffer, 0);
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
   public void handleKeyEvent(KeyEvent event) {
      screenWidget.handleKeyEvent(event);
   }

   @Override
   public void dispose() {
      log.info("Disposing GUI color buffers");
      colors.forEach(NanoVGColor::dispose);
      log.info("Disposed {} GUI colors", colors.size());

      log.info("Disposing GUI context");
      nvgDelete(context);
   }
}
