package com.bartlomiejpluta.base.api.internal.render;

import com.bartlomiejpluta.base.api.internal.gc.Cleanable;
import org.joml.*;

public interface ShaderManager extends Cleanable {
   ShaderManager createShader(String programName, String vertexShaderFilename, String fragmentShaderFilename);

   ShaderManager selectShader(String programName);

   ShaderManager useSelectedShader();

   ShaderManager detachCurrentShader();

   ShaderManager createUniform(String uniformName);

   ShaderManager createUniform(String uniformName, Uniform uniform);

   ShaderManager createUniforms(String uniformName, int size);

   ShaderManager createUniforms(String uniformName, int size, Uniform uniform);

   ShaderManager setUniform(String uniformName, int value);

   ShaderManager setUniform(String uniformName, boolean value);

   ShaderManager setUniform(String uniformName, float value);

   ShaderManager setUniform(String uniformName, Vector2f value);

   ShaderManager setUniform(String uniformName, Vector3f value);

   ShaderManager setUniform(String uniformName, Vector4f value);

   ShaderManager setUniform(String uniformName, Matrix3f value);

   ShaderManager setUniform(String uniformName, Matrix4f value);

   ShaderManager setUniform(String uniformName, Uniform uniform);

   ShaderManager setUniform(String uniformName, int index, Uniform uniform);

   ShaderManager setUniforms(String uniformName, Uniform[] uniforms);
}