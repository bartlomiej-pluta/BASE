package com.bartlomiejpluta.base.lib.db;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Relop {
   EQ("="),
   NE("<>"),
   GT(">"),
   GE(">="),
   LT("<"),
   LE("<=");

   @Getter
   private final String op;
}
