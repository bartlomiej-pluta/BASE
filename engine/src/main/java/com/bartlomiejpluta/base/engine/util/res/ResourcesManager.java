package com.bartlomiejpluta.base.engine.util.res;

import com.bartlomiejpluta.base.engine.error.AppException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Scanner;

@Component
public class ResourcesManager {
   public InputStream loadResourceAsStream(String fileName) {
      return ResourcesManager.class.getResourceAsStream(fileName);
   }

   public String loadResourceAsString(String fileName) {
      try (InputStream in = loadResourceAsStream(fileName);
           Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
         return scanner.useDelimiter("\\A").next();
      } catch (Exception e) {
         throw new AppException(e);
      }
   }

   public ByteBuffer loadResourceAsByteBuffer(String fileName) {
      try {
         var bytes = loadResourceAsStream(fileName).readAllBytes();
         return ByteBuffer
                 .allocateDirect(bytes.length)
                 .order(ByteOrder.nativeOrder())
                 .put(bytes)
                 .flip();
      } catch (IOException e) {
         throw new AppException(e);
      }
   }
}
