package com.bartlomiejpluta.base.internal.render;

import com.bartlomiejpluta.base.internal.gc.Disposable;
import org.joml.*;

public interface ShaderProgram extends Disposable {
   void createUniform(String uniformName);

   void createUniform(String uniformName, Uniform uniform);

   void createUniforms(String uniformName, int size);

   void createUniforms(String uniformName, int size, Uniform uniform);

   void setUniform(String uniformName, int value);

   void setUniform(String uniformName, boolean value);

   void setUniform(String uniformName, float value);

   void setUniform(String uniformName, Vector2fc value);

   void setUniform(String uniformName, Vector3fc value);

   void setUniform(String uniformName, Vector4fc value);

   void setUniform(String uniformName, Matrix3fc value);

   void setUniform(String uniformName, Matrix4fc value);

   void setUniform(String uniformName, Uniform uniform);

   void setUniform(String uniformName, int index, Uniform uniform);

   void setUniforms(String uniformName, Uniform[] uniforms);

   void use();

   void detach();
}
