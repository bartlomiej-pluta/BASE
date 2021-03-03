package com.bartlomiejpluta.base.engine.core.gl.render;

import com.bartlomiejpluta.base.engine.gc.Cleanable;
import com.bartlomiejpluta.base.engine.ui.Window;
import com.bartlomiejpluta.base.engine.world.camera.Camera;

public interface Renderer extends Cleanable {
   void init();
   void render(Window window, Camera camera, Renderable renderable);
}
