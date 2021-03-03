package com.bartlomiejpluta.base.engine.project.loader;

public interface ClassLoader {
   <T> Class<T> loadClass(String className);
}
