package com.bartlomiejpluta.base.core.gl.render;

import com.bartlomiejpluta.base.core.ui.Window;

public interface Renderer {
   void init();

   void render(Window window, Renderable renderable);

   void cleanUp();
}
