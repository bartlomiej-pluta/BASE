package com.bartlomiejpluta.base.engine.core.al.listener;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.alListener3f;

public class AudioListener {

   public AudioListener() {
      this(new Vector3f(0, 0, 0));
   }

   public AudioListener(Vector3fc position) {
      alListener3f(AL_POSITION, position.x(), position.y(), position.z());
      alListener3f(AL_VELOCITY, 0, 0, 0);
   }

   public void setPosition(Vector3fc position) {
      alListener3f(AL_POSITION, position.x(), position.y(), position.z());
   }

   public void setSpeed(Vector3fc speed) {
      alListener3f(AL_VELOCITY, speed.x(), speed.y(), speed.z());
   }

   public void setOrientation(Vector3fc at, Vector3fc up) {
      var data = new float[6];
      data[0] = at.x();
      data[1] = at.y();
      data[2] = at.z();
      data[3] = up.x();
      data[4] = up.y();
      data[5] = up.z();
      alListenerfv(AL_ORIENTATION, data);
   }
}
