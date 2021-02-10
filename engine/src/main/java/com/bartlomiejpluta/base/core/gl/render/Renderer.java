package com.bartlomiejpluta.base.core.gl.render;

import com.bartlomiejpluta.base.core.gc.Cleanable;
import com.bartlomiejpluta.base.core.ui.Window;

public interface Renderer extends Cleanable {
   void init();
   void render(Window window, Renderable renderable);
}
