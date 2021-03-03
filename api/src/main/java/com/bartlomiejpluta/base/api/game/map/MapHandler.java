package com.bartlomiejpluta.base.api.game.map;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.input.Keyboard;

public interface MapHandler {
   void init(Context context, GameMap map);

   void input(Keyboard keyboard);

   void update(Context context, GameMap map, float dt);

   void postRender(float windowWidth, float windowHeight);
}
