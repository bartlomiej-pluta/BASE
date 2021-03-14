package com.bartlomiejpluta.base.engine.common.manager;

import java.nio.ByteBuffer;

public interface ByteBufferAssetManager<A> {
   void registerAsset(A asset);

   ByteBuffer loadObjectByteBuffer(String uid);
}
