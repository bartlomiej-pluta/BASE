package com.bartlomiejpluta.base.engine.world.autotile.asset;

import com.bartlomiejpluta.base.engine.common.asset.Asset;
import com.bartlomiejpluta.base.engine.world.autotile.model.AutoTileLayout;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class AutoTileSetAsset extends Asset {
   private final int rows;
   private final int columns;
   private final AutoTileLayout layout;


   public AutoTileSetAsset(@NonNull String uid, @NonNull String source, int rows, int columns, @NonNull AutoTileLayout layout) {
      super(uid, source);
      this.rows = rows;
      this.columns = columns;
      this.layout = layout;
   }
}