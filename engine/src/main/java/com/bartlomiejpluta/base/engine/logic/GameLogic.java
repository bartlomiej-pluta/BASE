package com.bartlomiejpluta.base.engine.logic;

import com.bartlomiejpluta.base.api.internal.gc.Cleanable;
import com.bartlomiejpluta.base.api.internal.window.Window;

public interface GameLogic extends Cleanable {
   void init(Window window);

   void input(Window window);

   void update(float dt);

   void render(Window window);
}
