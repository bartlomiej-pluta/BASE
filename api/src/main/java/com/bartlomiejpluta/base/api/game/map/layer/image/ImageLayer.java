package com.bartlomiejpluta.base.api.game.map.layer.image;

import com.bartlomiejpluta.base.api.game.image.Image;
import com.bartlomiejpluta.base.api.game.map.layer.base.Layer;

public interface ImageLayer extends Layer {
   void setImage(Image image);

   void setOpacity(float opacity);

   void setPosition(float x, float y);

   void setMode(ImageLayerMode mode);

   void setScale(float scaleX, float scaleY);

   void setParallax(boolean parallax);
}
