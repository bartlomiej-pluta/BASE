package com.bartlomiejpluta.base.lib.db;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Ordering {
   ASC("ASC"),
   DESC("DESC");

   @Getter
   private final String ordering;
}
