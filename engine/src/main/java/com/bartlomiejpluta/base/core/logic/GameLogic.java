package com.bartlomiejpluta.base.core.logic;

import com.bartlomiejpluta.base.core.gc.Cleanable;
import com.bartlomiejpluta.base.core.ui.Window;

public interface GameLogic extends Cleanable {
   void init(Window window);

   void input(Window window);

   void update(float dt);

   void render(Window window);
}
