package com.bartlomiejpluta.base.engine.core.gl.render;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.internal.gc.Cleanable;
import com.bartlomiejpluta.base.internal.render.Renderable;

public interface Renderer extends Cleanable {
   void init();

   void render(Screen screen, Camera camera, Renderable renderable);
}
