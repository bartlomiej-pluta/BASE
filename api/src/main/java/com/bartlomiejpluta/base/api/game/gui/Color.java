package com.bartlomiejpluta.base.api.game.gui;

public interface Color {
   float getRed();

   float getGreen();

   float getBlue();

   float getAlpha();

   void setRed(float value);

   void setGreen(float value);

   void setBlue(float value);

   void setAlpha(float value);

   void setColor(float red, float green, float blue);

   void setColor(float red, float green, float blue, float alpha);
}
