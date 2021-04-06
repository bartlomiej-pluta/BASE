package com.bartlomiejpluta.base.api.move;

import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.event.EventType;
import com.bartlomiejpluta.base.lib.event.BaseEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class MoveEvent extends BaseEvent {
   public enum Action {BEGIN, END}

   private final Action action;
   private final Movable movable;
   private final Movement movement;

   @Override
   public EventType<? extends Event> getType() {
      return TYPE;
   }

   public static EventType<MoveEvent> TYPE = new EventType<>("MOVE_EVENT");
}
