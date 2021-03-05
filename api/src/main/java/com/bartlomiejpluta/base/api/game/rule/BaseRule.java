package com.bartlomiejpluta.base.api.game.rule;

import com.bartlomiejpluta.base.api.game.entity.Entity;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseRule implements Rule {
   private final List<Entity> invoked = new LinkedList<>();

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