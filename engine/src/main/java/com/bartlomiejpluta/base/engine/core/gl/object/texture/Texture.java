package com.bartlomiejpluta.base.engine.core.gl.object.texture;

import com.bartlomiejpluta.base.api.internal.gc.Disposable;
import com.bartlomiejpluta.base.engine.error.AppException;
import lombok.Getter;
import org.joml.Vector2f;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

public class Texture implements Disposable {
   private static final int DESIRED_CHANNELS = 4;

   private final int textureId;

   @Getter
   private final String fileName;

   @Getter
   private final int width;

   @Getter
   private final int height;

   @Getter
   private final int rows;

   @Getter
   private final int columns;

   @Getter
   private final Vector2f spriteFragment;

   @Getter
   private final Vector2f spriteSize;


   Texture(String textureFilename, ByteBuffer buffer, int rows, int columns) {
      try (var stack = MemoryStack.stackPush()) {
         var widthBuffer = stack.mallocInt(1);
         var heightBuffer = stack.mallocInt(1);
         var channelsBuffer = stack.mallocInt(1);

         buffer = stbi_load_from_memory(buffer, widthBuffer, heightBuffer, channelsBuffer, DESIRED_CHANNELS);
         if (buffer == null) {
            throw new AppException("Image file [%s] could not be loaded: %s", textureFilename, stbi_failure_reason());
         }

         width = widthBuffer.get();
         height = heightBuffer.get();

         textureId = glGenTextures();

         glBindTexture(GL_TEXTURE_2D, textureId);
         glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
         glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

         glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
         glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

         fileName = textureFilename;

         this.rows = rows;
         this.columns = columns;
         this.spriteFragment = new Vector2f(1 / (float) columns, 1 / (float) rows);
         this.spriteSize = new Vector2f(width, height).mul(spriteFragment);
      }
   }

   public void activate() {
      glActiveTexture(GL_TEXTURE0);
      glBindTexture(GL_TEXTURE_2D, textureId);
   }

   @Override
   public void dispose() {
      glDeleteTextures(textureId);
   }
}
