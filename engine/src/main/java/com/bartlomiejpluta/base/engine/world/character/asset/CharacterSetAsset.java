package com.bartlomiejpluta.base.engine.world.character.asset;

import com.bartlomiejpluta.base.engine.common.asset.Asset;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class CharacterSetAsset extends Asset {
   private final int rows;
   private final int columns;

   public CharacterSetAsset(@NonNull String uid, @NonNull String source, int rows, int columns) {
      super(uid, source);
      this.rows = rows;
      this.columns = columns;
   }
}
