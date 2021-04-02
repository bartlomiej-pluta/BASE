package com.bartlomiejpluta.base.engine.gui.render;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.gui.manager.FontManager;
import com.bartlomiejpluta.base.engine.gui.manager.WidgetDefinitionManager;
import com.bartlomiejpluta.base.engine.gui.widget.ScreenWidget;
import com.bartlomiejpluta.base.engine.gui.xml.inflater.Inflater;
import com.bartlomiejpluta.base.engine.world.image.manager.ImageManager;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static java.lang.Integer.toBinaryString;
import static java.lang.String.format;
import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.system.MemoryUtil.NULL;

@Slf4j
@Getter
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NanoVGGUI implements GUI {
   private final Context context;
   private final FontManager fontManager;
   private final ImageManager imageManager;
   private final Inflater inflater;
   private final WidgetDefinitionManager widgetDefinitionManager;

   private long nvg;
   private ScreenWidget screenWidget;

   private final List<NanoVGColor> colors = new LinkedList<>();
   private final List<NanoVGPaint> paints = new LinkedList<>();
   private final Set<String> loadedFonts = new HashSet<>();
   private final Map<String, NanoVGImage> loadedImages = new HashMap<>();

   private boolean visible = true;

   public void init(Screen screen) {
      nvg = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);

      if (nvg == NULL) {
         throw new AppException("Could not init NanoVG");
      }

      screenWidget = new ScreenWidget(screen);
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      if (!visible) {
         return;
      }

      nvgBeginFrame(nvg, screen.getWidth(), screen.getHeight(), 1);

      screenWidget.draw(screen, this);

      nvgEndFrame(nvg);
   }

   @Override
   public Component inflateComponent(String widgetUid) {
      log.info("Inflating component by widget definition with UID: [{}]", widgetUid);
      var is = widgetDefinitionManager.loadObject(widgetUid);
      return inflater.inflateComponent(is, context, this);
   }

   @Override
   public Window inflateWindow(String widgetUid) {
      log.info("Inflating window by widget definition with UID: [{}]", widgetUid);
      var is = widgetDefinitionManager.loadObject(widgetUid);
      return inflater.inflateWindow(is, context, this);
   }

   @Override
   public boolean isVisible() {
      return visible;
   }

   @Override
   public void setVisible(boolean visible) {
      this.visible = visible;
   }

   @Override
   public void show() {
      this.visible = true;
   }

   @Override
   public void hide() {
      this.visible = false;
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
      var color = new NanoVGColor(NVGColor.create());
      colors.add(color);
      return color;
   }

   @Override
   public Paint createPaint() {
      var paint = new NanoVGPaint(NVGPaint.create());
      paints.add(paint);
      return paint;
   }

   @Override
   public Image getImage(String imageUid) {
      return getImage(imageUid, 0);
   }

   @Override
   public Image getImage(String imageUid, int imageFlags) {
      var key = format("%s_%d", imageUid, imageFlags);
      var image = loadedImages.get(key);

      if (image == null) {
         log.info("Loading GUI image with UID: [{}] into cache under the key: [{}]", imageUid, key);
         var data = imageManager.loadObjectByteBuffer(imageUid);
         var handle = nvgCreateImageMem(nvg, imageFlags, data);
         var width = new int[1];
         var height = new int[1];
         nvgImageSize(nvg, handle, width, height);

         log.info("GUI image with UID: [{}], size {}x{} and flags [0b{}] has been loaded", imageUid, width[0], height[0], toBinaryString(imageFlags));
         image = new NanoVGImage(handle, width[0], height[0]);

         loadedImages.put(key, image);
      }

      return image;
   }

   @Override
   public void beginPath() {
      nvgBeginPath(nvg);
   }

   @Override
   public void closePath() {
      nvgClosePath(nvg);
   }

   @Override
   public void drawRectangle(float x, float y, float width, float height) {
      nvgRect(nvg, x, y, width, height);
   }

   @Override
   public void drawRoundedRectangle(float x, float y, float width, float height, float radius) {
      nvgRoundedRect(nvg, x, y, width, height, radius);
   }

   @Override
   public void drawCircle(float x, float y, float radius) {
      nvgCircle(nvg, x, y, radius);
   }

   @Override
   public void drawEllipse(float x, float y, float radiusX, float radiusY) {
      nvgEllipse(nvg, x, y, radiusX, radiusY);
   }

   @Override
   public void drawArc(float x, float y, float radius, float start, float end, WindingDirection direction) {
      nvgArc(nvg, x, y, radius, start, end, direction == WindingDirection.CLOCKWISE ? NVG_CW : NVG_CCW);
   }

   @Override
   public void drawArcTo(float x1, float y1, float x2, float y2, float radius) {
      nvgArcTo(nvg, x1, y1, x2, y2, radius);
   }

   @Override
   public void drawLineTo(float x, float y) {
      nvgLineTo(nvg, x, y);
   }

   @Override
   public void drawBezierTo(float controlX1, float controlY1, float controlX2, float controlY2, float targetX, float targetY) {
      nvgBezierTo(nvg, controlX1, controlY1, controlX2, controlY2, targetX, targetY);
   }

   @Override
   public void drawQuadBezierTo(float controlX, float controlY, float targetX, float targetY) {
      nvgQuadTo(nvg, controlX, controlY, targetX, targetY);
   }

   @Override
   public void setLineCap(LineCap cap) {
      nvgLineCap(nvg, lineCapToNanoVGInteger(cap));
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
      nvgLineJoin(nvg, lineCapToNanoVGInteger(join));
   }

   @Override
   public void moveTo(float x, float y) {
      nvgMoveTo(nvg, x, y);
   }

   @Override
   public void setGlobalAlpha(float alpha) {
      nvgGlobalAlpha(nvg, alpha);
   }

   @Override
   public void setPathWinding(WindingDirection direction) {
      nvgPathWinding(nvg, direction == WindingDirection.CLOCKWISE ? NVG_CW : NVG_CCW);
   }

   @Override
   public void setFillColor(Color color) {
      nvgFillColor(nvg, ((NanoVGColor) color).getColor());
   }

   @Override
   public void setFillPaint(Paint paint) {
      nvgFillPaint(nvg, ((NanoVGPaint) paint).getPaint());
   }

   @Override
   public void fill() {
      nvgFill(nvg);
   }

   @Override
   public void setStrokeColor(Color color) {
      nvgStrokeColor(nvg, ((NanoVGColor) color).getColor());
   }

   @Override
   public void setStrokePaint(Paint paint) {
      nvgStrokePaint(nvg, ((NanoVGPaint) paint).getPaint());
   }

   @Override
   public void stroke() {
      nvgStroke(nvg);
   }

   @Override
   public void boxGradient(float x, float y, float width, float height, float radius, float feather, Color inner, Color outer, Paint target) {
      nvgBoxGradient(
            nvg,
            x,
            y,
            width,
            height,
            radius,
            feather,
            ((NanoVGColor) inner).getColor(),
            ((NanoVGColor) outer).getColor(),
            ((NanoVGPaint) target).getPaint()
      );
   }

   @Override
   public void linearGradient(float x, float y, float endX, float endY, Color start, Color end, Paint target) {
      nvgLinearGradient(
            nvg,
            x,
            y,
            endX,
            endY,
            ((NanoVGColor) start).getColor(),
            ((NanoVGColor) end).getColor(),
            ((NanoVGPaint) target).getPaint()
      );
   }

   @Override
   public void radialGradient(float x, float y, float innerRadius, float outerRadius, Color start, Color end, Paint target) {
      nvgRadialGradient(
            nvg,
            x,
            y,
            innerRadius,
            outerRadius,
            ((NanoVGColor) start).getColor(),
            ((NanoVGColor) end).getColor(),
            ((NanoVGPaint) target).getPaint()
      );
   }

   @Override
   public void imagePattern(float x, float y, float angle, float alpha, Image image, Paint target) {
      nvgImagePattern(
            nvg,
            x,
            y,
            image.getWidth(),
            image.getHeight(),
            angle,
            ((NanoVGImage) image).getImageHandle(),
            alpha,
            ((NanoVGPaint) target).getPaint()
      );
   }

   @Override
   public void imagePattern(float x, float y, float width, float height, float angle, float alpha, Image image, Paint target) {
      nvgImagePattern(
            nvg,
            x,
            y,
            width,
            height,
            angle,
            ((NanoVGImage) image).getImageHandle(),
            alpha,
            ((NanoVGPaint) target).getPaint()
      );
   }

   @Override
   public void setStrokeWidth(float width) {
      nvgStrokeWidth(nvg, width);
   }

   @Override
   public void putText(float x, float y, CharSequence text, float[] outTextBounds) {
      nvgText(nvg, x, y, text);
      nvgTextBounds(nvg, x, y, text, outTextBounds);
   }

   @Override
   public void putText(float x, float y, CharSequence text) {
      nvgText(nvg, x, y, text);
   }

   @Override
   public void putTextBox(float x, float y, float lineWidth, CharSequence text, float[] outTextBounds) {
      nvgTextBox(nvg, x, y, lineWidth, text);
      nvgTextBoxBounds(nvg, x, y, lineWidth, text, outTextBounds);
   }

   @Override
   public void putTextBox(float x, float y, float lineWidth, CharSequence text) {
      nvgTextBox(nvg, x, y, lineWidth, text);
   }

   @Override
   public void setFontFace(String fontUid) {
      if (!loadedFonts.contains(fontUid)) {
         var fontBuffer = fontManager.loadObjectByteBuffer(fontUid);
         nvgCreateFontMem(nvg, fontUid, fontBuffer, 0);
         loadedFonts.add(fontUid);
      }

      nvgFontFace(nvg, fontUid);
   }

   @Override
   public void setFontSize(float size) {
      nvgFontSize(nvg, size);
   }

   @Override
   public void setFontBlur(float blur) {
      nvgFontBlur(nvg, blur);
   }

   @Override
   public void setTextAlignment(int alignment) {
      nvgTextAlign(nvg, alignment);
   }

   @Override
   public void setTextLineHeight(float textLineHeight) {
      nvgTextLineHeight(nvg, textLineHeight);
   }

   @Override
   public void clip(float x, float y, float width, float height) {
      nvgScissor(nvg, x, y, width, height);
   }

   @Override
   public void resetClip() {
      nvgResetScissor(nvg);
   }

   @Override
   public void dispose() {
      log.info("Disposing GUI color buffers");
      colors.forEach(NanoVGColor::dispose);
      log.info("Disposed {} GUI color buffers", colors.size());

      log.info("Disposing GUI paint buffers");
      paints.forEach(NanoVGPaint::dispose);
      log.info("Disposed {} GUI paint buffers", paints.size());

      log.info("Disposing GUI images");
      loadedImages.values().stream().map(NanoVGImage::getImageHandle).forEach(h -> nvgDeleteImage(nvg, h));
      log.info("Disposed {} GUI images", loadedImages.size());

      log.info("Disposing GUI nvg");
      nvgDelete(nvg);
   }
}
