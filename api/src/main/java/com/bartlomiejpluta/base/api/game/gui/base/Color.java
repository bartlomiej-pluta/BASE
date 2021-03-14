package com.bartlomiejpluta.base.api.game.gui.base;

public interface Color {
   void setRGB(float red, float green, float blue);

   void setRGBA(float red, float green, float blue, float alpha);

   void setRed(float value);

   void setGreen(float value);

   void setBlue(float value);

   void setAlpha(float value);

   float getRed();

   float getGreen();

   float getBlue();

   float getAlpha();
}
