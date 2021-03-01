package com.bartlomiejpluta.base.game.project.loader;

public interface ClassLoader {
   <T> Class<T> loadClass(String className);
}
