package com.bartlomiejpluta.samplegame.core.gl.render;

import com.bartlomiejpluta.samplegame.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.samplegame.core.gl.shader.program.ShaderProgram;

public interface Renderable {
   void render(ShaderManager shaderManager);
   void cleanUp();
}
