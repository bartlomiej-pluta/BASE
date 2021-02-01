package com.bartlomiejpluta.base.core.gl.render;

import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.ui.Window;

public interface Renderable {
   void render(Window window, ShaderManager shaderManager);
}
