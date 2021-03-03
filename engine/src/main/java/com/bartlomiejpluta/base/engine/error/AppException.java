package com.bartlomiejpluta.base.engine.error;

public class AppException extends RuntimeException {
   public AppException() {
   }

   public AppException(String message, Object... args) {
      super(String.format(message, args));
   }

   public AppException(String message, Throwable cause) {
      super(message, cause);
   }

   public AppException(Throwable cause) {
      super(cause);
   }

   public AppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
   }
}
