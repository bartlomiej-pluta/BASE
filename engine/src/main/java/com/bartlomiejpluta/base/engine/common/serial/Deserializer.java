package com.bartlomiejpluta.base.engine.common.serial;

import com.bartlomiejpluta.base.engine.error.AppException;

import java.io.InputStream;

public abstract class Deserializer<T> {
   protected abstract T parse(InputStream input) throws Exception;

   public T deserialize(InputStream input) {
      try {
         return parse(input);
      } catch (Exception e) {
         throw new AppException(e);
      }
   }
}
