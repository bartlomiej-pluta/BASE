package com.bartlomiejpluta.base.engine.world.tileset.asset;

import com.bartlomiejpluta.base.engine.common.asset.Asset;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class TileSetAsset extends Asset {
   private final int rows;
   private final int columns;

   public TileSetAsset(@NonNull String uid, @NonNull String source, int rows, int columns) {
      super(uid, source);
      this.rows = rows;
      this.columns = columns;
   }
}
