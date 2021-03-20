package com.bartlomiejpluta.base.api.game.animation;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.map.layer.base.Layer;
import org.joml.Vector2fc;

public interface AnimationRunner {
   void run(Context context, Layer layer, Vector2fc origin);
}
