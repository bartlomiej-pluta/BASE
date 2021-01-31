package com.bartlomiejpluta.base.core.logic;

import com.bartlomiejpluta.base.core.ui.Window;

public interface GameLogic {
   void init(Window window);

   void input(Window window);

   void update(float dt);

   void render(Window window);

   void cleanUp();
}
