package com.bartlomiejpluta.base.internal.render;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.screen.Screen;

public interface Renderable {
   void render(Screen screen, Camera camera, ShaderManager shaderManager);
}
