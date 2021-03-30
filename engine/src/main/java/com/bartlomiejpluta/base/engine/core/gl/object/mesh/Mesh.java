package com.bartlomiejpluta.base.engine.core.gl.object.mesh;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.internal.gc.Disposable;
import com.bartlomiejpluta.base.internal.render.Renderable;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryStack;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh implements Renderable, Disposable {
   private final int vaoId;
   private final List<Integer> vboIds = new ArrayList<>(2);
   private final int elementsCount;

   @Getter
   private final Vector2fc farthestVertex;

   @Getter
   private final Vector2fc primarySize;

   public Mesh(float[] vertices, float[] texCoords, int[] elements) {
      this.elementsCount = elements.length;

      var vboId = 0;
      try (var stack = MemoryStack.stackPush()) {
         vaoId = glGenVertexArrays();
         glBindVertexArray(vaoId);

         // Vertices VBO
         vboId = glGenBuffers();
         vboIds.add(vboId);
         glBindBuffer(GL_ARRAY_BUFFER, vboId);
         glBufferData(GL_ARRAY_BUFFER, stack.mallocFloat(vertices.length).put(vertices).flip(), GL_STATIC_DRAW);
         glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

         // Texture Coordinates VBO
         vboId = glGenBuffers();
         vboIds.add(vboId);
         glBindBuffer(GL_ARRAY_BUFFER, vboId);
         glBufferData(GL_ARRAY_BUFFER, stack.mallocFloat(texCoords.length).put(texCoords).flip(), GL_STATIC_DRAW);
         glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

         // Elements VBO
         vboId = glGenBuffers();
         vboIds.add(vboId);
         glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
         glBufferData(GL_ELEMENT_ARRAY_BUFFER, stack.mallocInt(elementsCount).put(elements).flip(), GL_STATIC_DRAW);

         glBindBuffer(GL_ARRAY_BUFFER, 0);
         glBindVertexArray(0);
      }

      var minX = Float.MAX_VALUE;
      var minY = Float.MAX_VALUE;
      var maxX = 0f;
      var maxY = 0f;

      for (int i = 0; i < vertices.length / 2; ++i) {
         var x = vertices[2 * i];
         var y = vertices[2 * i + 1];

         if (x < minX) {
            minX = x;
         }

         if (x > maxX) {
            maxX = x;
         }

         if (y < minY) {
            minY = y;
         }

         if (y > maxY) {
            maxY = y;
         }
      }

      farthestVertex = new Vector2f(abs(maxX) > abs(minX) ? maxX : minX, abs(maxY) > abs(minY) ? maxY : minY);
      primarySize = new Vector2f(maxX - minX, maxY - minY);
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      glBindVertexArray(vaoId);

      glEnableVertexAttribArray(0);
      glEnableVertexAttribArray(1);

      glDrawElements(GL_TRIANGLES, elementsCount, GL_UNSIGNED_INT, 0);

      glDisableVertexAttribArray(0);
      glDisableVertexAttribArray(1);

      glBindVertexArray(0);
   }

   @Override
   public void dispose() {
      glDisableVertexAttribArray(0);
      glDisableVertexAttribArray(1);

      glBindBuffer(GL_ARRAY_BUFFER, 0);
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

      vboIds.forEach(GL15::glDeleteBuffers);

      glBindVertexArray(0);
      glDeleteVertexArrays(vaoId);
   }

   public static Mesh quad(float width, float height, float originX, float originY) {
      var vertices = new float[] {
         -originX, -originY,
         -originX, height - originY,
         width - originX, height - originY,
         width - originX, - originY
      };

      var texCoords = new float[] { 0, 0, 0, 1, 1, 1, 1, 0 };

      var elements = new int[] { 0, 1, 2, 2, 3, 0 };

      return new Mesh(vertices, texCoords, elements);
   }
}
