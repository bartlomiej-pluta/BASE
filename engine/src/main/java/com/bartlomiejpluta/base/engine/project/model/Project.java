package com.bartlomiejpluta.base.engine.project.model;

import com.bartlomiejpluta.base.engine.world.entity.asset.EntitySetAsset;
import com.bartlomiejpluta.base.engine.world.image.asset.ImageAsset;
import com.bartlomiejpluta.base.engine.world.map.asset.GameMapAsset;
import com.bartlomiejpluta.base.engine.world.tileset.asset.TileSetAsset;
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

   @NonNull
   private final List<EntitySetAsset> entitySetAssets;
}
