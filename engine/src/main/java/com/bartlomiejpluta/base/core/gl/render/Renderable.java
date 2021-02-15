package com.bartlomiejpluta.base.core.gl.render;

import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.world.camera.Camera;

public interface Renderable {
   void render(Window window, Camera camera, ShaderManager shaderManager);
}
