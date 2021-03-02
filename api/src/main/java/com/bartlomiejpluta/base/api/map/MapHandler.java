package com.bartlomiejpluta.base.api.map;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.input.Keyboard;

public interface MapHandler {
   void init(Context context, GameMap map);

   void input(Keyboard keyboard);

   void update(Context context, GameMap map, float dt);
}
