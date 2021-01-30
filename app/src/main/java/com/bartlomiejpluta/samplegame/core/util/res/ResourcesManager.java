package com.bartlomiejpluta.samplegame.core.util.res;

import com.bartlomiejpluta.samplegame.core.error.AppException;
import org.springframework.stereotype.Component;

import java.io.InputStream;
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
}
