package com.bartlomiejpluta.samplegame.core.gl.object.mesh;

import com.bartlomiejpluta.samplegame.core.gl.render.Renderable;
import com.bartlomiejpluta.samplegame.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.samplegame.core.gl.shader.program.ShaderProgram;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryStack;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh implements Renderable {
   private final int vaoId;
   private final List<Integer> vboIds = new ArrayList<>(2);
   private final int elementsCount;

   public Mesh(float[] vertices, int[] elements) {
      try(var stack = MemoryStack.stackPush()) {
         elementsCount = elements.length;
         var verticesBuffer = stack.mallocFloat(vertices.length);
         var elementsBuffer = stack.mallocInt(elementsCount);
         verticesBuffer.put(vertices).flip();
         elementsBuffer.put(elements).flip();

         vaoId = glGenVertexArrays();
         glBindVertexArray(vaoId);

         int vboId = glGenBuffers();
         vboIds.add(vboId);
         glBindBuffer(GL_ARRAY_BUFFER, vboId);
         glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
         glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

         vboId = glGenBuffers();
         vboIds.add(vboId);
         glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
         glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementsBuffer, GL_STATIC_DRAW);

         glBindBuffer(GL_ARRAY_BUFFER, 0);
         glBindVertexArray(0);
      }
   }

   @Override
   public void render(ShaderManager shaderManager) {
      glBindVertexArray(vaoId);
      glEnableVertexAttribArray(0);
      glDrawElements(GL_TRIANGLES, elementsCount, GL_UNSIGNED_INT, 0);
      glDisableVertexAttribArray(0);
      glBindVertexArray(0);
   }

   @Override
   public void cleanUp() {
      glDisableVertexAttribArray(0);
      glBindBuffer(GL_ARRAY_BUFFER, 0);
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

      vboIds.forEach(GL15::glDeleteBuffers);

      glBindVertexArray(0);
      glDeleteVertexArrays(vaoId);
   }
}
