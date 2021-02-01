package com.bartlomiejpluta.base.core.gl.object.texture;

import com.bartlomiejpluta.base.core.gc.Cleanable;

public interface TextureManager extends Cleanable {
   Texture loadTexture(String textureFileName);
}
