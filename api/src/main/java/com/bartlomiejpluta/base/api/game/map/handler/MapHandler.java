package com.bartlomiejpluta.base.api.game.map.handler;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.map.model.GameMap;
import com.bartlomiejpluta.base.api.game.window.Window;

public interface MapHandler {
   void onCreate(Context context, GameMap map);

   void onOpen(Context context, GameMap map);

   void input(Window window);

   void update(Context context, GameMap map, float dt);

   void postRender(Window window);
}
