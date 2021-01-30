package com.bartlomiejpluta.samplegame.core.logic;

import com.bartlomiejpluta.samplegame.core.ui.Window;

public interface GameLogic {
   void init(Window window);

   void input(Window window);

   void update(float dt);

   void render(Window window);

   void cleanUp();
}
