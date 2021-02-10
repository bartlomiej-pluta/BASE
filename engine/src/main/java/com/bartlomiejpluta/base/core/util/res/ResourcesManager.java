package com.bartlomiejpluta.base.core.util.res;

import com.bartlomiejpluta.base.core.error.AppException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Scanner;

@Component
public class ResourcesManager {
   public String loadResourceAsString(String fileName) {
      try (InputStream in = ResourcesManager.class.getResourceAsStream(fileName);
           Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
         return scanner.useDelimiter("\\A").next();
      } catch (Exception e) {
         throw new AppException(e);
      }
   }

   public ByteBuffer loadResourceAsByteBuffer(String fileName) {
      try {
         var bytes = ResourcesManager.class.getResourceAsStream(fileName).readAllBytes();
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
