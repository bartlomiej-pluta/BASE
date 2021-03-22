package com.bartlomiejpluta.base.lib.animation;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.map.layer.base.Layer;
import org.joml.Vector2fc;

public interface AnimationRunner {
   void run(Context context, Layer layer, Vector2fc origin);
}
