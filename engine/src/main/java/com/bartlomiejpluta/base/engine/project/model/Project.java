package com.bartlomiejpluta.base.engine.project.model;

import com.bartlomiejpluta.base.engine.audio.asset.SoundAsset;
import com.bartlomiejpluta.base.engine.gui.asset.FontAsset;
import com.bartlomiejpluta.base.engine.gui.asset.WidgetDefinitionAsset;
import com.bartlomiejpluta.base.engine.world.animation.asset.AnimationAsset;
import com.bartlomiejpluta.base.engine.world.character.asset.CharacterSetAsset;
import com.bartlomiejpluta.base.engine.world.image.asset.ImageAsset;
import com.bartlomiejpluta.base.engine.world.map.asset.GameMapAsset;
import com.bartlomiejpluta.base.engine.world.tileset.asset.TileSetAsset;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
@Builder
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
   private final List<CharacterSetAsset> characterSetAssets;

   @NonNull
   private final List<AnimationAsset> animationAssets;

   @NonNull
   private final List<FontAsset> fontAssets;

   @NonNull
   private final List<WidgetDefinitionAsset> widgetDefinitionAssets;

   @NonNull
   private final List<SoundAsset> soundAssets;
}
