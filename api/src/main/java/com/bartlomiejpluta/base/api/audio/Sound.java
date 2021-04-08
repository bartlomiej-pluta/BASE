package com.bartlomiejpluta.base.api.audio;

public interface Sound {
   void play();

   void pause();

   void stop();

   boolean isPlaying();

   boolean isPaused();

   boolean isStopped();

   void setGain(float gain);

   void setRepeat(boolean repeat);
}
