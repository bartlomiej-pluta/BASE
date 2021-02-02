package com.bartlomiejpluta.base.core.image;

import com.bartlomiejpluta.base.core.gc.Cleanable;

public interface ImageManager extends Cleanable {
   Image createImage(String imageFileName);
}
