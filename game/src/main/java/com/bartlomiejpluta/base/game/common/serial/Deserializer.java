package com.bartlomiejpluta.base.game.common.serial;

import com.bartlomiejpluta.base.core.error.AppException;

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
