package com.bartlomiejpluta.base.game.common.manager;

public interface AssetManager<A, T> {
   void registerAsset(A asset);

   T loadObject(String uid);
}
