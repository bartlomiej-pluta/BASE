package com.bartlomiejpluta.base.engine.core.gl.shader.manager;

import com.bartlomiejpluta.base.engine.core.gl.shader.program.GLShaderProgram;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.util.res.ResourcesManager;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import com.bartlomiejpluta.base.internal.render.ShaderProgram;
import com.bartlomiejpluta.base.internal.render.Uniform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joml.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultShaderManager implements ShaderManager {
   private final ResourcesManager resourcesManager;
   private final Map<String, ShaderProgram> shaders = new HashMap<>();
   private final Map<String, AtomicInteger> counters = new HashMap<>();
   private ShaderProgram current;

   @Override
   public ShaderManager createShader(String programName, String vertexShaderFilename, String fragmentShaderFilename) {
      log.info("Creating {} shader", programName);
      var vertexShaderCode = resourcesManager.loadResourceAsString(vertexShaderFilename);
      var fragmentShaderCode = resourcesManager.loadResourceAsString(fragmentShaderFilename);
      var program = GLShaderProgram.compile(vertexShaderCode, fragmentShaderCode);

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
   public ShaderManager setUniform(String uniformName, Vector2fc value) {
      current.setUniform(uniformName, value);
      return this;
   }

   @Override
   public ShaderManager setUniform(String uniformName, Vector3fc value) {
      current.setUniform(uniformName, value);
      return this;
   }

   @Override
   public ShaderManager setUniform(String uniformName, Vector4fc value) {
      current.setUniform(uniformName, value);
      return this;
   }

   @Override
   public ShaderManager setUniform(String uniformName, Matrix3fc value) {
      current.setUniform(uniformName, value);
      return this;
   }

   @Override
   public ShaderManager setUniform(String uniformName, Matrix4fc value) {
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
   public ShaderManager createCounter(String counterName) {
      if (counters.containsKey(counterName)) {
         throw new AppException(format("The [%s] counter already exists", counterName));
      }

      log.info("Creating {} uniform counter", counterName);
      counters.put(counterName, new AtomicInteger(0));
      return this;
   }

   @Override
   public int nextNumber(String counterName) {
      return counters.get(counterName).getAndIncrement();
   }

   @Override
   public int topNumber(String counterName) {
      return counters.get(counterName).get();
   }

   @Override
   public ShaderManager setUniformCounter(String uniformName, String counterName) {
      setUniform(uniformName, counters.get(counterName).get());
      return this;
   }

   @Override
   public ShaderManager resetCounters() {
      for(var counter : counters.values()) {
         counter.set(0);
      }

      return this;
   }

   @Override
   public void cleanUp() {
      log.info("Disposing shaders");
      shaders.forEach((name, program) -> program.dispose());
      log.info("{} shaders have been disposed", shaders.size());
   }
}
