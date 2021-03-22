package com.bartlomiejpluta.base.api.screen;

import org.joml.Vector2fc;

public interface Screen {
   int getWidth();

   int getHeight();

   Vector2fc getSize();

   boolean isResized();

   void setResized(boolean resized);

   boolean shouldClose();

   void update();

   void clear(float r, float g, float b, float alpha);

   void restoreState();

   void init();

   long getID();
}
