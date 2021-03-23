package com.bartlomiejpluta.base.engine.audio.manager;

import com.bartlomiejpluta.base.api.audio.Sound;
import com.bartlomiejpluta.base.engine.audio.asset.SoundAsset;
import com.bartlomiejpluta.base.engine.core.al.engine.AudioEngine;
import com.bartlomiejpluta.base.engine.core.al.source.AudioSource;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.project.config.ProjectConfiguration;
import com.bartlomiejpluta.base.engine.util.res.ResourcesManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultSoundManager implements SoundManager {
   private final Map<String, SoundAsset> assets = new HashMap<>();
   private final Set<String> loadedBuffers = new HashSet<>();
   private final AudioEngine engine;
   private final ResourcesManager resourcesManager;
   private final ProjectConfiguration configuration;

   @Override
   public void registerAsset(SoundAsset asset) {
      log.info("Registering [{}] sound asset under UID: [{}]", asset.getSource(), asset.getUid());
      assets.put(asset.getUid(), asset);
   }

   @Override
   public Sound loadObject(String uid) {
      if (!loadedBuffers.contains(uid)) {
         log.info("Loading [{}] sound", uid);
         var asset = assets.get(uid);

         if (asset == null) {
            throw new AppException("The sound asset with UID: [%s] does not exist", uid);
         }

         var source = configuration.projectFile("audio", asset.getSource());
         var buffer = resourcesManager.loadResourceAsByteBuffer(source);
         engine.loadVorbis(asset.getUid(), buffer);
         loadedBuffers.add(uid);
      }

      return engine.createSource(uid);
   }

   @Override
   public void disposeSound(Sound sound) {
      engine.disposeSource((AudioSource) sound);
   }
}
