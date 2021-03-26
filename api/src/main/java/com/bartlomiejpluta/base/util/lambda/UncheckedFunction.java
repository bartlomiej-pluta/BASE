package com.bartlomiejpluta.base.util.lambda;

import java.util.Objects;

@FunctionalInterface
public interface UncheckedFunction<T, R, E extends Throwable> {

   R apply(T t) throws E;

   default <V> UncheckedFunction<V, R, E> compose(UncheckedFunction<? super V, ? extends T, ? extends E> before) {
      Objects.requireNonNull(before);
      return (V v) -> apply(before.apply(v));
   }

   default <V> UncheckedFunction<T, V, E> andThen(UncheckedFunction<? super R, ? extends V, ? extends E> after) {
      Objects.requireNonNull(after);
      return (T t) -> after.apply(apply(t));
   }

   static <T, E extends Throwable> UncheckedFunction<T, T, E> identity() {
      return t -> t;
   }
}
