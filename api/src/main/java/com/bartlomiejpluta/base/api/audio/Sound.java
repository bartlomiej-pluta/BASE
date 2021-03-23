package com.bartlomiejpluta.base.api.audio;

public interface Sound {
   void play();

   void pause();

   void stop();

   boolean isPlaying();

   void setGain(float gain);

   void setRepeat(boolean repeat);
}
