package com.bartlomiejpluta.base.api.game.map.layer.color;

import com.bartlomiejpluta.base.api.game.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.internal.object.Placeable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;

public interface ColorLayer extends Placeable, Renderable, Layer {
   void setColor(float red, float green, float blue, float alpha);

   void setColor(float red, float green, float blue);

   void setRed(float red);

   void setGreen(float green);

   void setBlue(float blue);

   void setAlpha(float alpha);
}
