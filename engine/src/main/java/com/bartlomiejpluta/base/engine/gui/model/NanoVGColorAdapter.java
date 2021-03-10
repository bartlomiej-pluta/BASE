package com.bartlomiejpluta.base.engine.gui.model;

import com.bartlomiejpluta.base.api.game.gui.Color;
import com.bartlomiejpluta.base.api.internal.gc.Disposable;
import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGColor;

public class NanoVGColorAdapter extends NVGColor implements Color, Disposable {

   public NanoVGColorAdapter(float red, float green, float blue, float alpha) {
      super(BufferUtils.createByteBuffer(SIZEOF));

      r(red);
      g(green);
      b(blue);
      a(alpha);
   }

   @Override
   public float getRed() {
      return r();
   }

   @Override
   public float getGreen() {
      return g();
   }

   @Override
   public float getBlue() {
      return b();
   }

   @Override
   public float getAlpha() {
      return a();
   }

   @Override
   public void setRed(float value) {
      r(value);
   }

   @Override
   public void setGreen(float value) {
      g(value);
   }

   @Override
   public void setBlue(float value) {
      b(value);
   }

   @Override
   public void setAlpha(float value) {
      a(value);
   }

   @Override
   public void setColor(float red, float green, float blue) {
      r(red);
      g(green);
      b(blue);
   }

   @Override
   public void setColor(float red, float green, float blue, float alpha) {
      r(red);
      g(green);
      b(blue);
      a(alpha);
   }

   @Override
   public void dispose() {
      free();
   }
}
