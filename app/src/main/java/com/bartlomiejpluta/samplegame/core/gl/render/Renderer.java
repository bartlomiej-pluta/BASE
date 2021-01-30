package com.bartlomiejpluta.samplegame.core.gl.render;

import com.bartlomiejpluta.samplegame.core.ui.Window;

public interface Renderer {
   void init();

   void render(Window window);

   void cleanUp();
}
