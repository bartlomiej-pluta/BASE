package com.bartlomiejpluta.base.api.map.handler;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.input.Input;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.api.screen.Screen;

public interface MapHandler {
   void onCreate(Context context, GameMap map);

   void onOpen(Context context, GameMap map);

   void input(Input input);

   void update(Context context, GameMap map, float dt);

   void postRender(Screen screen);
}
