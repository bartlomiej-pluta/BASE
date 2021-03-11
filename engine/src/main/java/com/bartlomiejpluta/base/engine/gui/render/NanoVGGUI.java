package com.bartlomiejpluta.base.engine.gui.render;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.gui.base.Widget;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.gui.manager.FontManager;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.nanovg.NVGColor;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.system.MemoryUtil.NULL;

@Getter
public class NanoVGGUI implements GUI {
   private long context;

   @Setter
   private Widget root;

   private NVGColor color;
   private final Set<String> loadedFonts = new HashSet<>();
   private final float[] boundBuffer = new float[4];

   private final FontManager fontManager;

   public NanoVGGUI(FontManager fontManager) {
      this.fontManager = fontManager;
   }

   public void init(Screen screen) {
      context = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);

      color = NVGColor.create();

      if (context == NULL) {
         throw new AppException("Could not onCreate NanoVG");
      }
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      nvgBeginFrame(context, screen.getWidth(), screen.getHeight(), 1);

      if (root != null) {
         root.draw(screen, this);
      }

      nvgEndFrame(context);
   }

   @Override
   public void beginPath() {
      nvgBeginPath(context);
   }

   @Override
   public void drawRectangle(float x, float y, float w, float h) {
      nvgRect(context, x, y, w, h);
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
   public void fillColor(float red, float green, float blue, float alpha) {
      color.r(red);
      color.g(green);
      color.b(blue);
      color.a(alpha);

      nvgFillColor(context, color);
      nvgFill(context);
   }

   @Override
   public void strokeColor(float red, float green, float blue, float alpha) {
      color.r(red);
      color.g(green);
      color.b(blue);
      color.a(alpha);

      nvgStrokeColor(context, color);
      nvgFill(context);
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
   public void setTextAlignment(int alignment) {
      nvgTextAlign(context, alignment);
   }

   @Override
   public void dispose() {
      color.free();
      nvgDelete(context);
   }
}
