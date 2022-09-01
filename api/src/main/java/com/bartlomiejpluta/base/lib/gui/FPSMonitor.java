package com.bartlomiejpluta.base.lib.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.Attribute;
import com.bartlomiejpluta.base.api.gui.Color;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.util.profiler.FPSProfiler;

import java.util.Map;

public class FPSMonitor extends BaseComponent {
   private final Color color;
   private final Color background;
   private FPSProfiler fpsProfiler = FPSProfiler.create(5, 40);
   private float lineWidth = 1f;

   public FPSMonitor(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
      color = gui.createColor();
      background = gui.createColor();
      color.setRGBA(0xFF0000FF);
      background.setRGBA(0x444444AA);
   }

   @Attribute("monitor")
   public void setMonitor(Integer[] options) {
      if (options.length != 2) {
         throw new IllegalArgumentException("Expected 2 parameters: batch size and number of samples");
      }

      this.fpsProfiler = FPSProfiler.create(options[0], options[1]);
   }

   public void setColor(Integer hex) {
      color.setRGBA(hex);
   }

   public void setBackground(Integer hex) {
      background.setRGBA(hex);
   }

   public void setLineWidth(Float width) {
      this.lineWidth = width;
   }

   @Override
   protected float getContentWidth() {
      return width;
   }

   @Override
   protected float getContentHeight() {
      return height;
   }

   @Override
   public void update(float dt) {
      super.update(dt);
      fpsProfiler.update(dt);
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      var values = fpsProfiler.getSamples();
      var actualHeight = height - paddingTop - paddingBottom;
      var step = (width - paddingLeft - paddingRight) / values.size();

      gui.beginPath();
      gui.drawRectangle(x, y, width, height);
      gui.setFillColor(background);
      gui.fill();
      gui.closePath();

      gui.beginPath();
      gui.moveTo(x + paddingLeft, (float) (actualHeight - values.get(0) / 60 * actualHeight + y + paddingTop));

      for (int i = 1; i < values.size(); ++i) {
         gui.drawLineTo(i * step + x + paddingLeft, (float) (actualHeight - values.get(i) / 60 * actualHeight + y + paddingTop));
      }

      gui.setStrokeColor(color);
      gui.setStrokeWidth(lineWidth);
      gui.stroke();
      gui.closePath();
   }
}
