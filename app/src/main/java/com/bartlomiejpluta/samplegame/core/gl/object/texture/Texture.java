package com.bartlomiejpluta.samplegame.core.gl.object.texture;

import com.bartlomiejpluta.samplegame.core.error.AppException;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
   private static final int DESIRED_CHANNELS = 4;

   private final int textureId;
   private final int width;
   private final int height;

   Texture(String textureFilename, ByteBuffer buffer) {
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
         glGenerateMipmap(GL_TEXTURE_2D);
      }
   }

   public void activate() {
      glActiveTexture(GL_TEXTURE0);
      glBindTexture(GL_TEXTURE_2D, textureId);
   }
}
