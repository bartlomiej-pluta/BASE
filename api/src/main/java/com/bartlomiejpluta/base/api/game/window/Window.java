package com.bartlomiejpluta.base.api.game.window;

import com.bartlomiejpluta.base.api.game.input.Key;
import org.joml.Vector2f;

public interface Window {
   void init();

   int getWidth();

   int getHeight();

   Vector2f getSize();

   boolean isResized();

   void setResized(boolean resized);

   boolean shouldClose();

   boolean isKeyPressed(Key key);

   void update();

   void clear(float r, float g, float b, float alpha);
}
