package com.bartlomiejpluta.base.engine.core.engine;

import com.bartlomiejpluta.base.api.context.Context;

public interface GameEngine {
   void start(Context context);

   void stop();

   boolean isRunning();
}
