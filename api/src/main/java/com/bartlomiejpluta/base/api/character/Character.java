package com.bartlomiejpluta.base.api.character;

import com.bartlomiejpluta.base.api.animation.Animated;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.move.Movable;
import com.bartlomiejpluta.base.api.move.Movement;

import java.util.concurrent.CompletableFuture;

public interface Character extends Movable, Animated, Entity {

   Movement move(Direction direction);

   Direction getFaceDirection();

   void setFaceDirection(Direction direction);

   void changeCharacterSet(String characterSetUid);

   CompletableFuture<Void> performInstantAnimation();
}
