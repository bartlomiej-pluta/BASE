package com.bartlomiejpluta.base.engine.common.asset;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class Asset {

   @NonNull
   protected final String uid;

   @NonNull
   protected final String source;
}
