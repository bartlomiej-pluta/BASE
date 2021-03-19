package com.bartlomiejpluta.base.engine.world.animation.asset;

import com.bartlomiejpluta.base.engine.common.asset.Asset;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class AnimationAsset extends Asset {
   private final int rows;
   private final int columns;
   private final String framesSignature;

   public AnimationAsset(@NonNull String uid, @NonNull String source, int rows, int columns) {
      super(uid, source);
      this.rows = rows;
      this.columns = columns;
      this.framesSignature = String.format("%dx%d", rows, columns);
   }

   public String framesSignature() {
      return framesSignature;
   }
}
