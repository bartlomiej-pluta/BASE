package com.bartlomiejpluta.base.engine.core.gl.object.texture;

import com.bartlomiejpluta.base.engine.gc.Cleanable;

public interface TextureManager extends Cleanable {
   Texture loadTexture(String textureFileName);
   Texture loadTexture(String textureFileName, int rows, int columns);
}
