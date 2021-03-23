package com.bartlomiejpluta.base.api.audio;

import org.joml.Vector3fc;

public interface Sound {
   void play();

   void pause();

   void stop();

   boolean isPlaying();

   void setGain(float gain);

   void setRepeat(boolean repeat);

   void setRelative(boolean relative);

   void setPosition(Vector3fc position);

   void setSpeed(Vector3fc speed);
}
