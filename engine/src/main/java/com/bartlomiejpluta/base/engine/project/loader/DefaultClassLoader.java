package com.bartlomiejpluta.base.engine.project.loader;

import com.bartlomiejpluta.base.engine.error.AppException;
import org.springframework.stereotype.Component;

@Component
public class DefaultClassLoader implements ClassLoader {

   @Override
   @SuppressWarnings("unchecked")
   public <T> Class<T> loadClass(String className) {
      try {
         return (Class<T>) getClass().getClassLoader().loadClass(className);
      } catch (ClassNotFoundException e) {
         throw new AppException(e);
      }
   }
}
