package com.bartlomiejpluta.base.engine.world.autotile.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AutoTileLayout {
   LAYOUT_2X2(2, 2),
   LAYOUT_2X3(2, 3);

   private final int columns;
   private final int rows;
}
