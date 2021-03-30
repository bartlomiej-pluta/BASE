package com.bartlomiejpluta.base.api.rule;

import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.move.Movement;
import org.joml.Vector2ic;

public interface MovementRule {
   Vector2ic from();

   Vector2ic to();

   void invoke(Movement movement);
}
