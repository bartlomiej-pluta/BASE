package com.bartlomiejpluta.base.api.event;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@RequiredArgsConstructor
public final class EventType<E extends Event> {
   private final String type;
}
