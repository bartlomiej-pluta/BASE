package com.bartlomiejpluta.base.api.game.rule;

import com.bartlomiejpluta.base.api.game.entity.Entity;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseRule implements Rule {
   private final Set<Entity> invoked = new HashSet<>();

   protected abstract boolean test(Entity entity);

   protected abstract void invoke(Entity entity);

   @Override
   public boolean when(Entity entity) {
      if (test(entity)) {
         return true;
      }

      invoked.remove(entity);
      return false;
   }

   @Override
   public void then(Entity entity) {
      if (!invoked.contains(entity)) {
         invoke(entity);
         invoked.add(entity);
      }
   }
}