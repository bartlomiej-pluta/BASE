package com.bartlomiejpluta.base.game.image.manager;

import com.bartlomiejpluta.base.core.gc.Cleanable;
import com.bartlomiejpluta.base.game.image.asset.ImageAsset;
import com.bartlomiejpluta.base.game.image.model.Image;

public interface ImageManager extends Cleanable {
   void registerAsset(ImageAsset asset);

   Image loadImage(String uid);
}
