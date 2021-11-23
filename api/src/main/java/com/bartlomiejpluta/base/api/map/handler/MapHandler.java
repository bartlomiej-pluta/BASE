package com.bartlomiejpluta.base.api.map.handler;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.input.Input;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.api.screen.Screen;

public interface MapHandler {
   default void onCreate(Context context, GameMap map) {
      // do nothing
   }

   default void onOpen(Context context, GameMap map) {
      // do nothing
   }

   default void input(Input input) {
      // do nothing
   }

   default void update(Context context, GameMap map, float dt) {
      // do nothing
   }

   default void postRender(Screen screen) {
      // do nothing
   }
}
