package com.bartlomiejpluta.base.engine.world.image.manager;

import com.bartlomiejpluta.base.api.internal.gc.Cleanable;
import com.bartlomiejpluta.base.engine.common.manager.AssetManager;
import com.bartlomiejpluta.base.engine.world.image.asset.ImageAsset;
import com.bartlomiejpluta.base.engine.world.image.model.Image;

public interface ImageManager extends AssetManager<ImageAsset, Image>, Cleanable {
}
