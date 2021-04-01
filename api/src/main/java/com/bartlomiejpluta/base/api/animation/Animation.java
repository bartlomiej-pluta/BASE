package com.bartlomiejpluta.base.api.animation;

import com.bartlomiejpluta.base.api.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.move.Movable;
import com.bartlomiejpluta.base.internal.logic.Updatable;
import com.bartlomiejpluta.base.internal.render.Renderable;
import com.bartlomiejpluta.base.util.path.Path;

public interface Animation extends Movable, Animated, Renderable, Updatable {

   Integer getRepeat();

   void setRepeat(Integer repeat);

   void followPath(Path<Animation> path, Integer repeat, boolean finishOnEnd, boolean finishOnFail);

   void onAdd(Layer layer);

   void onFinish(Layer layer);

   void finish();

   boolean finished();
}
