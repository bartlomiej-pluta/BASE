package com.bartlomiejpluta.base.api.internal.render;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.window.Window;

public interface Renderable {
   void render(Window window, Camera camera, ShaderManager shaderManager);
}
