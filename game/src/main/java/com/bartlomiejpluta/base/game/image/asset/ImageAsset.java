package com.bartlomiejpluta.base.game.image.asset;

import com.bartlomiejpluta.base.game.common.asset.Asset;
import lombok.NonNull;

public class ImageAsset extends Asset {
   public ImageAsset(@NonNull String uid, @NonNull String source) {
      super(uid, source);
   }
}
