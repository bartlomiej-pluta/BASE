package com.bartlomiejpluta.base.api.game.animation;

import com.bartlomiejpluta.base.api.game.entity.Movable;
import com.bartlomiejpluta.base.api.game.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.internal.logic.Updatable;
import com.bartlomiejpluta.base.api.internal.object.Placeable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;
import com.bartlomiejpluta.base.api.util.path.Path;

public interface Animation extends Placeable, Movable, Renderable, Updatable {

   void setAnimationSpeed(float speed);

   Integer getRepeat();

   void setRepeat(Integer repeat);

   void followPath(Path<Animation> path, Integer repeat, boolean finishOnEnd, boolean finishOnFail);

   void onAdd(Layer layer);

   void onFinish(Layer layer);

   void finish();

   boolean finished();
}
