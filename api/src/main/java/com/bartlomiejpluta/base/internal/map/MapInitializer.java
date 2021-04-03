package com.bartlomiejpluta.base.internal.map;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.map.handler.MapHandler;

public interface MapInitializer {
   void run(final Context context, final MapHandler handler);
}
