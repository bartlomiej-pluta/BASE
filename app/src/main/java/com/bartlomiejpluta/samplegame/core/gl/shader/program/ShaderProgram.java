package com.bartlomiejpluta.samplegame.core.gl.shader.program;

import com.bartlomiejpluta.samplegame.core.error.AppException;
import com.bartlomiejpluta.samplegame.core.gl.shader.uniform.Uniform;
import lombok.extern.slf4j.Slf4j;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static org.lwjgl.opengl.GL20.*;

@Slf4j
public class ShaderProgram {
   private final int programId;
   private final int vertexShaderId;
   private final int fragmentShaderId;
   private final Map<String, Integer> uniforms = new HashMap<>();

   private ShaderProgram(String vertexShaderCode, String fragmentShaderCode) {
      this.programId = glCreateProgram();

      if(this.programId == 0) {
         throw new AppException("Could not create shader program");
      }

      this.vertexShaderId = createShader(vertexShaderCode, GL_VERTEX_SHADER);
      this.fragmentShaderId = createShader(fragmentShaderCode, GL_FRAGMENT_SHADER);

      linkProgram();
   }

   private int createShader(String shaderCode, int shaderType) {
      int shaderId = glCreateShader(shaderType);
      if(shaderId == 0) {
         throw new AppException("Could not create shader of type: %s", shaderType);
      }

      glShaderSource(shaderId, shaderCode);
      glCompileShader(shaderId);

      if(glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
         throw new AppException("Could not compile shader code: %s", glGetShaderInfoLog(shaderId, 1024));
      }

      glAttachShader(programId, shaderId);

      return shaderId;
   }

   private void linkProgram() {
      glLinkProgram(programId);
      if(glGetProgrami(programId, GL_LINK_STATUS) == 0) {
         throw new AppException("Could not link shader program: %s", glGetProgramInfoLog(programId, 1024));
      }

      if(vertexShaderId != 0) {
         glDetachShader(programId, vertexShaderId);
      }

      if(fragmentShaderId != 0) {
         glDetachShader(programId, fragmentShaderId);
      }

      glValidateProgram(programId);
      if(glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
         log.warn("Program validation failed: {}", glGetProgramInfoLog(programId, 1024));
      }
   }

   public void createUniform(String uniformName) {
      int location = glGetUniformLocation(programId, uniformName);

      if(location < 0) {
         throw new AppException("Could not find uniform: %s", uniformName);
      }

      uniforms.put(uniformName, location);
   }

   public void createUniform(String uniformName, Uniform uniform) {
      uniform.createUniform(this, uniformName);
   }

   public void createUniforms(String uniformName, int size) {
      for(int i=0; i<size; ++i) {
         createUniform(format("%s[%d]", uniformName, i));
      }
   }

   public void createUniforms(String uniformName, int size, Uniform uniform) {
      for(int i=0; i<size; ++i) {
         createUniform(format("%s[%d]", uniformName, i), uniform);
      }
   }

   public void setUniform(String uniformName, int value) {
      glUniform1i(uniforms.get(uniformName), value);
   }

   public void setUniform(String uniformName, boolean value) {
      glUniform1i(uniforms.get(uniformName), value ? 1 : 0);
   }

   public void setUniform(String uniformName, float value) {
      glUniform1f(uniforms.get(uniformName), value);
   }

   public void setUniform(String uniformName, Vector3f value) {
      glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
   }

   public void setUniform(String uniformName, Vector4f value) {
      glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
   }

   public void setUniform(String uniformName, Matrix3f value) {
      try(var stack = MemoryStack.stackPush()) {
         var buffer = stack.mallocFloat(3 * 3);
         value.get(buffer);
         glUniformMatrix4fv(uniforms.get(uniformName), false, buffer);
      }
   }

   public void setUniform(String uniformName, Matrix4f value) {
      try(var stack = MemoryStack.stackPush()) {
         var buffer = stack.mallocFloat(4 * 4);
         value.get(buffer);
         glUniformMatrix4fv(uniforms.get(uniformName), false, buffer);
      }
   }

   public void setUniform(String uniformName, Uniform uniform) {
      uniform.setUniform(this, uniformName);
   }

   public void setUniform(String uniformName, int index, Uniform uniform) {
      setUniform(format("%s[%d]", uniformName, index), uniform);
   }

   public void setUniforms(String uniformName, Uniform[] uniforms) {
      var size = uniforms != null ? uniforms.length : 0;
      for(int i=0; i<size; ++i) {
         setUniform(format("%s[%d]", uniformName, i), uniforms[i]);
      }
   }

   public void use() {
      glUseProgram(programId);
   }

   public void detach() {
      glUseProgram(0);
   }

   public void cleanUp() {
      glUseProgram(0);

      if(programId != 0) {
         glDeleteProgram(programId);
      }
   }

   public static ShaderProgram compile(String vertexShaderCode, String fragmentShaderCode) {
      return new ShaderProgram(vertexShaderCode, fragmentShaderCode);
   }
}
