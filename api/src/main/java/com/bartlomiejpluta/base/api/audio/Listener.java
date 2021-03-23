package com.bartlomiejpluta.base.api.audio;

import org.joml.Vector3fc;

public interface Listener {

   void setPosition(Vector3fc position);

   void setSpeed(Vector3fc speed);

   void setOrientation(Vector3fc at, Vector3fc up);
}
