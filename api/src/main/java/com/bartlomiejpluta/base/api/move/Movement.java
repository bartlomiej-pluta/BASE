package com.bartlomiejpluta.base.api.move;

import org.joml.Vector2ic;

public interface Movement {

   Vector2ic getFrom();

   Vector2ic getTo();

   Direction getDirection();

   boolean perform();

   void abort();

   void onFinish();
}
