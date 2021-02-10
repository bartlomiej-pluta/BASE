package com.bartlomiejpluta.base.core.gl.shader.uniform;

import com.bartlomiejpluta.base.core.gl.shader.program.ShaderProgram;

public interface Uniform {
   void createUniform(ShaderProgram shaderProgram, String uniformName);

   void setUniform(ShaderProgram shaderProgram, String uniformName);
}
