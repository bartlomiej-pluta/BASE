package com.bartlomiejpluta.base.core.gl.shader.manager;

import com.bartlomiejpluta.base.core.gl.shader.program.ShaderProgram;
import com.bartlomiejpluta.base.core.gl.shader.uniform.Uniform;
import com.bartlomiejpluta.base.core.util.res.ResourcesManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joml.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultShaderManager implements ShaderManager {
   private final ResourcesManager resourcesManager;
   private final Map<String, ShaderProgram> shaders = new HashMap<>();
   private ShaderProgram current;

   @Override
   public ShaderManager createShader(String programName, String vertexShaderFilename, String fragmentShaderFilename) {
      log.info("Creating {} shader", programName);
      var vertexShaderCode = resourcesManager.loadResourceAsString(vertexShaderFilename);
      var fragmentShaderCode = resourcesManager.loadResourceAsString(fragmentShaderFilename);
      var program = ShaderProgram.compile(vertexShaderCode, fragmentShaderCode);

      shaders.put(programName, program);

      return this;
   }

   @Override
   public ShaderManager selectShader(String programName) {
      current = shaders.get(programName);
      return this;
   }

   @Override
   public ShaderManager useSelectedShader() {
      current.use();
      return this;
   }

   @Override
   public ShaderManager detachCurrentShader() {
      current.detach();
      return this;
   }

   @Override
   public ShaderManager createUniform(String uniformName) {
      current.createUniform(uniformName);
      return this;
   }

   @Override
   public ShaderManager createUniform(String uniformName, Uniform uniform) {
      current.createUniform(uniformName, uniform);
      return this;
   }

   @Override
   public ShaderManager createUniforms(String uniformName, int size) {
      current.createUniforms(uniformName, size);
      return this;
   }

   @Override
   public ShaderManager createUniforms(String uniformName, int size, Uniform uniform) {
      current.createUniforms(uniformName, size, uniform);
      return this;
   }

   @Override
   public ShaderManager setUniform(String uniformName, int value) {
      current.setUniform(uniformName, value);
      return this;
   }

   @Override
   public ShaderManager setUniform(String uniformName, boolean value) {
      current.setUniform(uniformName, value);
      return this;
   }

   @Override
   public ShaderManager setUniform(String uniformName, float value) {
      current.setUniform(uniformName, value);
      return this;
   }

   @Override
   public ShaderManager setUniform(String uniformName, Vector2f value) {
      current.setUniform(uniformName, value);
      return this;
   }

   @Override
   public ShaderManager setUniform(String uniformName, Vector3f value) {
      current.setUniform(uniformName, value);
      return this;
   }

   @Override
   public ShaderManager setUniform(String uniformName, Vector4f value) {
      current.setUniform(uniformName, value);
      return this;
   }

   @Override
   public ShaderManager setUniform(String uniformName, Matrix3f value) {
      current.setUniform(uniformName, value);
      return this;
   }

   @Override
   public ShaderManager setUniform(String uniformName, Matrix4f value) {
      current.setUniform(uniformName, value);
      return this;
   }

   @Override
   public ShaderManager setUniform(String uniformName, Uniform uniform) {
      current.setUniform(uniformName, uniform);
      return this;
   }

   @Override
   public ShaderManager setUniform(String uniformName, int index, Uniform uniform) {
      current.setUniform(uniformName, index, uniform);
      return this;
   }

   @Override
   public ShaderManager setUniforms(String uniformName, Uniform[] uniforms) {
      current.setUniforms(uniformName, uniforms);
      return this;
   }

   @Override
   public void cleanUp() {
      shaders.forEach((name, program) -> program.cleanUp());
   }
}
