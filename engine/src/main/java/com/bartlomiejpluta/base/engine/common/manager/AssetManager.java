package com.bartlomiejpluta.base.engine.common.manager;

public interface AssetManager<A, T> {
   void registerAsset(A asset);

   T loadObject(String uid);
}
