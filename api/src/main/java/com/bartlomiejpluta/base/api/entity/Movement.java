package com.bartlomiejpluta.base.api.entity;

import com.bartlomiejpluta.base.api.geo.Vector;

public interface Movement {
   boolean perform();

   Movement another();

   Vector getFrom();

   Vector getTo();

   Direction getDirection();
}
