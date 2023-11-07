package com.bartlomiejpluta.base.api.light;

import com.bartlomiejpluta.base.api.location.Locationable;
import com.bartlomiejpluta.base.internal.logic.Updatable;
import com.bartlomiejpluta.base.internal.render.Renderable;
import org.joml.Vector3fc;

public interface Light extends Locationable, Updatable, Renderable {
   boolean isLuminescent();

   void setLuminescent(boolean luminescent);

   default void enableLuminescent() {
      setLuminescent(true);
   }

   default void disableLuminescent() {
      setLuminescent(false);
   }

   default void toggleLuminescent() {
      setLuminescent(!isLuminescent());
   }

   Vector3fc getIntensity();

   default void setIntensity(Vector3fc intensity) {
      setIntensity(intensity.x(), intensity.y(), intensity.z());
   }

   void setIntensity(float red, float green, float blue);

   float getConstantAttenuation();

   float getLinearAttenuation();

   float getQuadraticAttenuation();

   void setConstantAttenuation(float attenuation);

   void setLinearAttenuation(float attenuation);

   void setQuadraticAttenuation(float attenuation);

   default void setAttenuation(float constant, float linear, float quadratic) {
      setConstantAttenuation(constant);
      setLinearAttenuation(linear);
      setQuadraticAttenuation(quadratic);
   }

   default void setAttenuation(Vector3fc attenuation) {
      setConstantAttenuation(attenuation.x());
      setLinearAttenuation(attenuation.y());
      setQuadraticAttenuation(attenuation.z());
   }
}
