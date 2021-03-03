package com.bartlomiejpluta.base.engine.world.image.asset;

import com.bartlomiejpluta.base.engine.common.asset.Asset;
import lombok.NonNull;

public class ImageAsset extends Asset {
   public ImageAsset(@NonNull String uid, @NonNull String source) {
      super(uid, source);
   }
}
