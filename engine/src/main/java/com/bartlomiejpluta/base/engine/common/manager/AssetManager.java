package com.bartlomiejpluta.base.engine.common.manager;

public interface AssetManager<A, T> {
   void registerAsset(A asset);

   A getAsset(String uid);

   T loadObject(String uid);
}
