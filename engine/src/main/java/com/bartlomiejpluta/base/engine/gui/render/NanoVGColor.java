package com.bartlomiejpluta.base.engine.gui.render;

import com.bartlomiejpluta.base.api.game.gui.base.Color;
import com.bartlomiejpluta.base.api.internal.gc.Disposable;
import lombok.*;
import org.lwjgl.nanovg.NVGColor;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class NanoVGColor implements Color, Disposable {

   @NonNull
   private final NVGColor color;

   @Override
   public void setRGB(float red, float green, float blue) {
      color.r(red);
      color.g(green);
      color.b(blue);
   }

   @Override
   public void setRGBA(float red, float green, float blue, float alpha) {
      color.r(red);
      color.g(green);
      color.b(blue);
      color.a(alpha);
   }

   @Override
   public void setRGB(int hex) {
      color.r(((hex & 0xFF0000) >> 2 * 8) / 255f);
      color.g(((hex & 0x00FF00) >> 8) / 255f);
      color.b(((hex & 0x0000FF)) / 255f);
   }

   @Override
   public void setRGBA(long hex) {
      color.r(((hex & 0xFF000000L) >> 3 * 8) / 255f);
      color.g(((hex & 0x00FF0000L) >> 2 * 8) / 255f);
      color.b(((hex & 0x0000FF00L) >> 8) / 255f);
      color.a(((hex & 0x000000FFL)) / 255f);
   }

   @Override
   public void setRed(float value) {
      color.r(value);
   }

   @Override
   public void setGreen(float value) {
      color.g(value);
   }

   @Override
   public void setBlue(float value) {
      color.b(value);
   }

   @Override
   public void setAlpha(float value) {
      color.a(value);
   }

   @Override
   public void setRed(int value) {
      color.r(value / 255f);
   }

   @Override
   public void setGreen(int value) {
      color.g(value / 255f);
   }

   @Override
   public void setBlue(int value) {
      color.b(value / 255f);
   }

   @Override
   public void setAlpha(int value) {
      color.a(value / 255f);
   }

   @Override
   public float getRed() {
      return color.r();
   }

   @Override
   public float getGreen() {
      return color.g();
   }

   @Override
   public float getBlue() {
      return color.b();
   }

   @Override
   public float getAlpha() {
      return color.a();
   }

   @Override
   public void dispose() {
      color.free();
   }
}
