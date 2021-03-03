package com.bartlomiejpluta.base.engine.core.gl.render;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.window.Window;
import com.bartlomiejpluta.base.api.internal.gc.Cleanable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;

public interface Renderer extends Cleanable {
   void init();
   void render(Window window, Camera camera, Renderable renderable);
}
