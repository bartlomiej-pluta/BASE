package com.bartlomiejpluta.base.api.game.entity;


import org.joml.Vector2i;

public interface Movement {
   boolean perform();

   Movement another();

   Vector2i getFrom();

   Vector2i getTo();

   Direction getDirection();
}
