package com.bartlomiejpluta.base.api.context;

import lombok.Getter;
import lombok.NonNull;

public enum ContextHolder {
   INSTANCE;

   @Getter
   private Context context;

   public void setContext(@NonNull Context context) {
      if(this.context != null) {
         throw new IllegalStateException("Context cannot be reassigned");
      }

      this.context = context;
   }
}
