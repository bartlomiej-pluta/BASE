package com.bartlomiejpluta.base.game.tileset.asset;

import com.bartlomiejpluta.base.game.common.asset.Asset;
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
