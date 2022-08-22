package com.bartlomiejpluta.base.api.character;

import com.bartlomiejpluta.base.api.animation.Animated;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.event.EventType;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.move.Movable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface Character extends Movable, Animated, Entity {

   Direction getFaceDirection();

   void setFaceDirection(Direction direction);

   void changeCharacterSet(String characterSetUid);

   CompletableFuture<Void> performInstantAnimation();

   <E extends Event> void addEventListener(EventType<E> type, Consumer<E> listener);

   <E extends Event> void removeEventListener(EventType<E> type, Consumer<E> listener);
}
