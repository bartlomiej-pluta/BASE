package com.bartlomiejpluta.samplegame.core.gl.render;

import com.bartlomiejpluta.samplegame.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.samplegame.core.ui.Window;

public interface Renderable {
   void render(Window window, ShaderManager shaderManager);
   void cleanUp();
}
