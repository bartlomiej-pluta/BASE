package com.bartlomiejpluta.base.engine.logic;

import com.bartlomiejpluta.base.engine.gc.Cleanable;
import com.bartlomiejpluta.base.engine.ui.Window;

public interface GameLogic extends Cleanable {
   void init(Window window);

   void input(Window window);

   void update(float dt);

   void render(Window window);
}
