package com.bartlomiejpluta.base.engine.util.reflection;

public interface ClassLoader {
   <T> Class<T> loadClass(String className);
}
