package com.bartlomiejpluta.base.engine.util.reflection;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultClassLoader implements ClassLoader {

   @SneakyThrows
   @Override
   @SuppressWarnings("unchecked")
   public <T> Class<T> loadClass(String className) {
      log.info("Loading [{}] class", className);
      return (Class<T>) getClass().getClassLoader().loadClass(className);
   }
}
