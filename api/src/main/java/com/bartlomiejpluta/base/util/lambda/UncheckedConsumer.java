package com.bartlomiejpluta.base.util.lambda;

import java.util.Objects;

@FunctionalInterface
public interface UncheckedConsumer<T, E extends Throwable> {

   void accept(T t) throws E;

   default UncheckedConsumer<T, E> andThen(UncheckedConsumer<? super T, ? extends E> after) {
      Objects.requireNonNull(after);
      return (T t) -> {
         accept(t);
         after.accept(t);
      };
   }
}
