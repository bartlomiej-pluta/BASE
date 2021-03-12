package com.bartlomiejpluta.base.api.game.runner;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.input.Input;
import com.bartlomiejpluta.base.api.internal.gc.Disposable;

public interface GameRunner extends Disposable {
   void init(Context context);

   void input(Input input);

   void update(float dt);
}
