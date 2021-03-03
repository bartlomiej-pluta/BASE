package com.bartlomiejpluta.base.api.internal.window;

import org.joml.Vector2f;

public interface Window {
   void init();

   int getWidth();

   int getHeight();

   Vector2f getSize();

   boolean isResized();

   void setResized(boolean resized);

   boolean shouldClose();

   boolean isKeyPressed(int keyCode);

   void update();

   void clear(float r, float g, float b, float alpha);
}
