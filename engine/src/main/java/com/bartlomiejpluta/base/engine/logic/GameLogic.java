package com.bartlomiejpluta.base.engine.logic;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.internal.gc.Cleanable;

public interface GameLogic extends Cleanable {
   void init(Screen screen, Context context);

   void input();

   void update(float dt);

   void render(Screen screen);
}
