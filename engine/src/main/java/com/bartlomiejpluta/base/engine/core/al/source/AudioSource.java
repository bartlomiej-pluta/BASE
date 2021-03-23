package com.bartlomiejpluta.base.engine.core.al.source;

import com.bartlomiejpluta.base.api.audio.Sound;
import com.bartlomiejpluta.base.engine.core.al.buffer.AudioBuffer;
import com.bartlomiejpluta.base.internal.gc.Disposable;
import org.joml.Vector3fc;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.alGenSources;

public class AudioSource implements Sound, Disposable {
   private final int id;

   public AudioSource(AudioBuffer buffer) {
      this.id = alGenSources();

      stop();
      alSourcei(id, AL_BUFFER, buffer.getId());
   }

   public void setParameter(int param, float value) {
      alSourcef(id, param, value);
   }

   public void setPosition(Vector3fc position) {
      alSource3f(id, AL_POSITION, position.x(), position.y(), position.z());
   }

   public void setSpeed(Vector3fc speed) {
      alSource3f(id, AL_VELOCITY, speed.x(), speed.y(), speed.z());
   }

   public void setRelative(boolean relative) {
      alSourcei(id, AL_SOURCE_RELATIVE, relative ? AL_TRUE : AL_FALSE);
   }

   @Override
   public void setGain(float gain) {
      alSourcef(id, AL_GAIN, gain);
   }

   @Override
   public void play() {
      alSourcePlay(id);
   }

   @Override
   public void pause() {
      alSourcePause(id);
   }

   @Override
   public void stop() {
      alSourceStop(id);
   }

   @Override
   public boolean isPlaying() {
      return alGetSourcei(id, AL_SOURCE_STATE) == AL_PLAYING;
   }

   @Override
   public void setRepeat(boolean repeat) {
      alSourcei(id, AL_LOOPING, repeat ? AL_TRUE : AL_FALSE);
   }

   @Override
   public void dispose() {
      stop();
      alDeleteSources(id);
   }
}
