package com.bartlomiejpluta.base.api.map.layer.base;

import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.api.event.Reactive;
import com.bartlomiejpluta.base.api.light.Light;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.internal.program.Updatable;
import com.bartlomiejpluta.base.internal.render.Renderable;

import java.util.List;

public interface Layer extends Renderable, Updatable, Reactive {
   GameMap getMap();

   void pushAnimation(Animation animation);

   void addLight(Light light);

   void removeLight(Light light);

   void clearLights();

   List<Light> getLights();
}
