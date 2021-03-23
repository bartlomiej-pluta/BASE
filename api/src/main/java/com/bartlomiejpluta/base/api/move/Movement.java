package com.bartlomiejpluta.base.api.move;

import org.joml.Vector2ic;

public interface Movement {
   boolean perform();

   Movement another();

   Vector2ic getFrom();

   Vector2ic getTo();

   Direction getDirection();
}