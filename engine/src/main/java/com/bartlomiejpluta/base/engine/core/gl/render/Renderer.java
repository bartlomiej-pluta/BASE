package com.bartlomiejpluta.base.engine.core.gl.render;

import com.bartlomiejpluta.base.api.internal.camera.Camera;
import com.bartlomiejpluta.base.api.internal.gc.Cleanable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;
import com.bartlomiejpluta.base.api.internal.window.Window;

public interface Renderer extends Cleanable {
   void init();
   void render(Window window, Camera camera, Renderable renderable);
}
