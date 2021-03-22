package com.bartlomiejpluta.base.api.gui.base;

public interface Color {
   void setRGB(float red, float green, float blue);

   void setRGBA(float red, float green, float blue, float alpha);

   void setRGB(int hex);

   void setRGBA(long hex);

   void setRed(float value);

   void setGreen(float value);

   void setBlue(float value);

   void setAlpha(float value);

   void setRed(int value);

   void setGreen(int value);

   void setBlue(int value);

   void setAlpha(int value);

   float getRed();

   float getGreen();

   float getBlue();

   float getAlpha();
}
