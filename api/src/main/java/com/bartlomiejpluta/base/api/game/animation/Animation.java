package com.bartlomiejpluta.base.api.game.animation;

import com.bartlomiejpluta.base.api.game.entity.Movable;
import com.bartlomiejpluta.base.api.game.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.internal.logic.Updatable;
import com.bartlomiejpluta.base.api.internal.object.Placeable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;
import com.bartlomiejpluta.base.api.util.path.Path;
import org.joml.Vector2ic;

public interface Animation extends Placeable, Movable, Renderable, Updatable {

   void setStepSize(float x, float y);

   Vector2ic getCoordinates();

   void setCoordinates(Vector2ic coordinates);

   void setCoordinates(int x, int y);

   void setAnimationSpeed(float speed);

   Integer getRepeat();

   void setRepeat(Integer repeat);

   void followPath(Path<Animation> path, boolean repeat);

   void setSpeed(float speed);

   void onAdd(Layer layer);

   boolean finished();
}
