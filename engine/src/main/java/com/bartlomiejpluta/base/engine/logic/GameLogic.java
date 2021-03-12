package com.bartlomiejpluta.base.engine.logic;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.input.Input;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.gc.Cleanable;

public interface GameLogic extends Cleanable {
   void init(Screen screen, Context context);

   void input(Input input);

   void update(float dt);

   void render(Screen screen);
}
