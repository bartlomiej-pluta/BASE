package com.bartlomiejpluta.base.api.game.image;

import com.bartlomiejpluta.base.api.internal.object.Placeable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;

public interface Image extends Placeable, Renderable {
   int getPrimaryWidth();

   int getPrimaryHeight();

   int getFactor();

   int getWidth();

   int getHeight();
}
