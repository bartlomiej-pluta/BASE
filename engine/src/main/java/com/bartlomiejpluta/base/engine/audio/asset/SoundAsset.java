package com.bartlomiejpluta.base.engine.audio.asset;

import com.bartlomiejpluta.base.engine.common.asset.Asset;
import lombok.NonNull;

public class SoundAsset extends Asset {
   public SoundAsset(@NonNull String uid, @NonNull String source) {
      super(uid, source);
   }
}
