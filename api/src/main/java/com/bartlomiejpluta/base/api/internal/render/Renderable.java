package com.bartlomiejpluta.base.api.internal.render;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.screen.Screen;

public interface Renderable {
   void render(Screen screen, Camera camera, ShaderManager shaderManager);
}
