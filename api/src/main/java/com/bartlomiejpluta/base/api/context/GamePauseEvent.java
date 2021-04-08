package com.bartlomiejpluta.base.api.context;

import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.event.EventType;
import com.bartlomiejpluta.base.lib.event.BaseEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class GamePauseEvent extends BaseEvent {
   public static final EventType<GamePauseEvent> TYPE = new EventType<>("GAME_PAUSE_EVENT");
   boolean paused;

   @Override
   public EventType<? extends Event> getType() {
      return TYPE;
   }
}
