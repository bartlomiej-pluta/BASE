package com.bartlomiejpluta.base.lib.event;

import com.bartlomiejpluta.base.api.event.Event;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public abstract class BaseEvent implements Event {

   @Getter
   private boolean consumed = false;

   @Override
   public void consume() {
      consumed = true;
   }
}
