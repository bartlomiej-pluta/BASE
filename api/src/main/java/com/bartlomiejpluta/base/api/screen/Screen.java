package com.bartlomiejpluta.base.api.screen;

import org.joml.Vector2fc;
import org.joml.Vector2ic;

public interface Screen {
   int getWidth();

   int getHeight();

   Vector2fc getSize();

   boolean isResized();

   Vector2ic getCurrentResolution();

   void setResized(boolean resized);

   void setFullscreenMode();

   void setFullscreenMode(int width, int height);

   void setWindowMode(int xPos, int yPos, int width, int height);

   void setPosition(int x, int y);

   void setSize(int width, int height);

   void setResizable(boolean resizable);

   void show();

   void hide();

   boolean shouldClose();

   void update();

   void clear(float r, float g, float b, float alpha);

   void restoreState();

   void init();

   long getID();
}
