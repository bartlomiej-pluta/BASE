package com.bartlomiejpluta.base.api.runner;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.input.Input;
import com.bartlomiejpluta.base.internal.gc.Disposable;

public interface GameRunner extends Disposable {
   default void init(Context context) {
      // do nothing
   }

   default void input(Input input) {
      // do nothing
   }

   default void update(float dt) {
      // do nothing
   }

   @Override
   default void dispose() {
      // do nothing
   }
}
