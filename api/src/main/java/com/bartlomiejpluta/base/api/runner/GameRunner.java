package com.bartlomiejpluta.base.api.runner;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.input.Input;
import com.bartlomiejpluta.base.internal.gc.Disposable;

public interface GameRunner extends Disposable {
   void init(Context context);

   void input(Input input);

   void update(float dt);
}
