package com.bartlomiejpluta.base.engine.core.al.engine;

import com.bartlomiejpluta.base.engine.core.al.buffer.AudioBuffer;
import com.bartlomiejpluta.base.engine.core.al.listener.AudioListener;
import com.bartlomiejpluta.base.engine.core.al.source.AudioSource;
import com.bartlomiejpluta.base.engine.error.AppException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC11.alcOpenDevice;
import static org.lwjgl.system.MemoryUtil.NULL;

@Slf4j
@Component
public class DefaultAudioEngine implements AudioEngine {
   private final Map<String, AudioBuffer> buffers = new HashMap<>();
   private final List<AudioSource> sources = new LinkedList<>();

   private long device;
   private long context;

   @Getter
   private AudioListener listener;

   @Override
   public void init() {
      log.info("Initializing default audio device");
      device = alcOpenDevice((ByteBuffer) null);

      if (device == NULL) {
         throw new AppException("Failed to open the default OpenAL device");
      }

      log.info("Initializing audio context");
      var deviceCapabilities = ALC.createCapabilities(device);
      context = alcCreateContext(device, (IntBuffer) null);

      if (context == NULL) {
         throw new AppException("Failed to create OpenAL context");
      }

      alcMakeContextCurrent(context);
      AL.createCapabilities(deviceCapabilities);

      log.info("Initializing audio listener");
      listener = new AudioListener();
   }

   @Override
   public void loadVorbis(String name, ByteBuffer vorbis) {
      var buffer = new AudioBuffer(vorbis);
      buffers.put(name, buffer);
   }

   @Override
   public AudioSource createSource(String name) {
      var buffer = buffers.get(name);

      if (buffer == null) {
         throw new AppException("Audio buffer with name [%s] does not exist", name);
      }

      var source = new AudioSource();
      source.setBuffer(buffer);

      sources.add(source);

      return source;
   }

   @Override
   public void disposeSource(AudioSource source) {
      source.dispose();
      sources.remove(source);
   }

   @Override
   public void cleanUp() {
      log.info("Disposing audio sources");
      sources.forEach(AudioSource::dispose);
      log.info("{} audio sources have been disposed", sources.size());

      log.info("Disposing audio buffers");
      buffers.forEach((name, buffer) -> buffer.dispose());
      log.info("{} audio buffers have been disposed", buffers.size());

      if (context != NULL) {
         log.info("Disposing audio context");
         alcDestroyContext(context);
      } else {
         log.warn("Audio context is NULL and will not be disposed");
      }

      if (device != NULL) {
         log.info("Closing audio device");
         alcCloseDevice(device);
      } else {
         log.warn("Audio device is NULL and will not be closed");
      }
   }
}
