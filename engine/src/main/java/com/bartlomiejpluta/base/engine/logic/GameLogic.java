package com.bartlomiejpluta.base.engine.logic;

import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.gc.Cleanable;

public interface GameLogic extends Cleanable {
   void init(Screen screen);

   void input(Screen screen);

   void update(float dt);

   void render(Screen screen);
}
