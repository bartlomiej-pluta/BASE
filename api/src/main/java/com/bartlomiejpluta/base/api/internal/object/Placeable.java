package com.bartlomiejpluta.base.api.internal.object;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public interface Placeable {
   Vector2f getPosition();
   void setPosition(float x, float y);
   void setPosition(Vector2f position);
   void movePosition(float x, float y);
   void movePosition(Vector2f position);

   float getRotation();
   void setRotation(float rotation);
   void moveRotation(float rotation);

   float getScaleX();
   void setScaleX(float scale);

   float getScaleY();
   void setScaleY(float scale);

   void setScale(float scale);
   void setScale(float scaleX, float scaleY);

   Matrix4f getModelMatrix();
}
