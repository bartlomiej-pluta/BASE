package com.bartlomiejpluta.base.api.map.layer.base;

import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.internal.logic.Updatable;
import com.bartlomiejpluta.base.internal.render.Renderable;

public interface Layer extends Renderable, Updatable {
   GameMap getMap();

   void pushAnimation(Animation animation);
}
