package com.bartlomiejpluta.base.engine.gui.manager;

import com.bartlomiejpluta.base.engine.gui.asset.FontAsset;
import com.bartlomiejpluta.base.engine.world.tileset.asset.TileSetAsset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class DefaultFontManager implements FontManager {
   private final Map<String, TileSetAsset> assets = new HashMap<>();

   @Override
   public void registerAsset(FontAsset asset) {
      log.info("Registering [{}] font asset under UID: [{}]", asset.getSource(), asset.getUid());
   }

   @Override
   public Integer loadObject(String uid) {
      // TODO(Implement some kind of font object)
      // The method should return useful resource associated with the font
      // that can be easily consumed by NanoVG.
      // Likely it will be just integer with a unique font ID.
      return null;
   }
}
