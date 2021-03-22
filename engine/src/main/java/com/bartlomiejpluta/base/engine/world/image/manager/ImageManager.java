package com.bartlomiejpluta.base.engine.world.image.manager;

import com.bartlomiejpluta.base.api.image.Image;
import com.bartlomiejpluta.base.engine.common.manager.AssetManager;
import com.bartlomiejpluta.base.engine.common.manager.ByteBufferAssetManager;
import com.bartlomiejpluta.base.engine.world.image.asset.ImageAsset;
import com.bartlomiejpluta.base.internal.gc.Cleanable;

public interface ImageManager extends AssetManager<ImageAsset, Image>, ByteBufferAssetManager<ImageAsset>, Cleanable {
}
