package com.bartlomiejpluta.base.game.project.model;

import com.bartlomiejpluta.base.game.image.asset.ImageAsset;
import com.bartlomiejpluta.base.game.map.asset.GameMapAsset;
import com.bartlomiejpluta.base.game.tileset.asset.TileSetAsset;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class Project {

   @NonNull
   private final String name;

   @NonNull
   private final String runner;

   @NonNull
   private final List<TileSetAsset> tileSetAssets;

   @NonNull
   private final List<GameMapAsset> mapAssets;

   @NonNull
   private final List<ImageAsset> imageAssets;
}
