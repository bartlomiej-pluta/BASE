package com.bartlomiejpluta.base.api.internal.object;

import org.joml.Matrix4fc;
import org.joml.Vector2fc;

public interface Placeable {
   Vector2fc getPosition();

   void setPosition(float x, float y);

   void setPosition(Vector2fc position);

   void movePosition(float x, float y);

   void movePosition(Vector2fc position);

   float getRotation();

   void setRotation(float rotation);

   void moveRotation(float rotation);

   float getScaleX();

   void setScaleX(float scale);

   float getScaleY();

   void setScaleY(float scale);

   void setScale(float scale);

   void setScale(float scaleX, float scaleY);

   float euclideanDistance(Placeable other);

   Matrix4fc getModelMatrix();
}
