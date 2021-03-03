package com.bartlomiejpluta.base.engine.core.gl.render;

import com.bartlomiejpluta.base.engine.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.engine.ui.Window;
import com.bartlomiejpluta.base.engine.world.camera.Camera;

public interface Renderable {
   void render(Window window, Camera camera, ShaderManager shaderManager);
}
