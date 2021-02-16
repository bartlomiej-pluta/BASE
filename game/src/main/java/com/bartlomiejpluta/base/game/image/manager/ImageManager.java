package com.bartlomiejpluta.base.game.image.manager;

import com.bartlomiejpluta.base.core.gc.Cleanable;
import com.bartlomiejpluta.base.game.image.model.Image;

public interface ImageManager extends Cleanable {
   Image createImage(String imageFileName);
}
