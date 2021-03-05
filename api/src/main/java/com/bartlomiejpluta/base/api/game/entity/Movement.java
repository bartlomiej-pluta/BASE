package com.bartlomiejpluta.base.api.game.entity;


import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import org.joml.Vector2i;

public interface Movement {
   boolean perform(ObjectLayer layer);

   Movement another();

   Vector2i getFrom();

   Vector2i getTo();

   Direction getDirection();
}
