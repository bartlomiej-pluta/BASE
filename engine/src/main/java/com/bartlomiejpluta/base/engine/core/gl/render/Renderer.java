package com.bartlomiejpluta.base.engine.core.gl.render;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.gc.Cleanable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;

public interface Renderer extends Cleanable {
   void init();

   void render(Screen screen, Camera camera, Renderable renderable);
}
