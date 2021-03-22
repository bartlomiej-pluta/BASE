package com.bartlomiejpluta.base.api.image;

import com.bartlomiejpluta.base.internal.object.Placeable;
import com.bartlomiejpluta.base.internal.render.Renderable;

public interface Image extends Placeable, Renderable {
   int getPrimaryWidth();

   int getPrimaryHeight();

   int getFactor();

   int getWidth();

   int getHeight();

   void setOpacity(float opacity);

   float getOpacity();
}
