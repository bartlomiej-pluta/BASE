package com.bartlomiejpluta.base.engine.gui.render;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.gui.Color;
import com.bartlomiejpluta.base.api.game.gui.GUI;
import com.bartlomiejpluta.base.api.game.gui.Widget;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.gui.manager.FontManager;
import com.bartlomiejpluta.base.engine.gui.model.NanoVGColorAdapter;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.system.MemoryUtil.NULL;

@Getter
public class NanoVGGUI implements GUI {
   private long context;

   @Setter
   private Widget root;

   private final List<NanoVGColorAdapter> createdColors = new LinkedList<>();
   private final Set<String> loadedFonts = new HashSet<>();

   private final FontManager fontManager;

   public NanoVGGUI(FontManager fontManager) {
      this.fontManager = fontManager;
   }

   public void init(Screen screen) {
      context = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);

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
   public void fillColor(Color color) {
      nvgFillColor(context, (NanoVGColorAdapter) color);
      nvgFill(context);
   }

   @Override
   public void putText(float x, float y, CharSequence text) {
      nvgText(context, x, y, text);
   }

   @Override
   public Color createColor(float red, float green, float blue, float alpha) {
      var color = new NanoVGColorAdapter(red, green, blue, alpha);
      createdColors.add(color);
      return color;
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
   public void dispose() {
      createdColors.forEach(NanoVGColorAdapter::dispose);
      nvgDelete(context);
   }
}
