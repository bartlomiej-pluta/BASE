package com.bartlomiejpluta.base.api.internal.render;

public interface Uniform {
   void createUniform(ShaderProgram shaderProgram, String uniformName);

   void setUniform(ShaderProgram shaderProgram, String uniformName);
}
