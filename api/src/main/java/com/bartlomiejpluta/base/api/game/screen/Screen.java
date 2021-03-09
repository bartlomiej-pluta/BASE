package com.bartlomiejpluta.base.api.game.screen;

import com.bartlomiejpluta.base.api.game.input.Key;
import org.joml.Vector2fc;

public interface Screen {
   void init();

   int getWidth();

   int getHeight();

   Vector2fc getSize();

   boolean isResized();

   void setResized(boolean resized);

   boolean shouldClose();

   boolean isKeyPressed(Key key);

   void update();

   void clear(float r, float g, float b, float alpha);
}
