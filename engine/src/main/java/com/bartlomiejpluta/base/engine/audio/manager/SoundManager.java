package com.bartlomiejpluta.base.engine.audio.manager;

import com.bartlomiejpluta.base.api.audio.Sound;
import com.bartlomiejpluta.base.engine.audio.asset.SoundAsset;
import com.bartlomiejpluta.base.engine.common.manager.AssetManager;

public interface SoundManager extends AssetManager<SoundAsset, Sound> {
   void disposeSound(Sound sound);
}
