package com.bartlomiejpluta.base.util.collection;

import lombok.RequiredArgsConstructor;

import java.util.LinkedList;

@RequiredArgsConstructor
public class LimitedQueue<E> extends LinkedList<E> {
   private final int limit;

   @Override
   public boolean add(E o) {
      super.add(o);

      while (size() > limit) {
         super.remove();
      }

      return true;
   }
}