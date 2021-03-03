package com.bartlomiejpluta.base.engine.world.object;

import com.bartlomiejpluta.base.api.internal.object.Placeable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static java.lang.Math.toRadians;

@EqualsAndHashCode
public abstract class Model implements Placeable {
   private final Matrix4f modelMatrix = new Matrix4f();

   @Getter
   protected final Vector2f position = new Vector2f(0, 0);

   @Getter
   @Setter
   protected float rotation;

   @Getter
   @Setter
   protected float scaleX = 1.0f;

   @Getter
   @Setter
   protected float scaleY = 1.0f;

   @Override
   public void setPosition(float x, float y) {
      position.x = x;
      position.y = y;
   }

   @Override
   public void setPosition(Vector2f position) {
      this.position.x = position.x;
      this.position.y = position.y;
   }

   @Override
   public void movePosition(float x, float y) {
      position.x += x;
      position.y += y;
   }

   @Override
   public void movePosition(Vector2f position) {
      this.position.x += position.x;
      this.position.y += position.y;
   }

   @Override
   public void moveRotation(float rotation) {
      this.rotation += rotation;
   }

   @Override
   public void setScale(float scale) {
      this.scaleX = scale;
      this.scaleY = scale;
   }

   @Override
   public void setScale(float scaleX, float scaleY) {
      this.scaleX = scaleX;
      this.scaleY = scaleY;
   }

   @Override
   public Matrix4f getModelMatrix() {
      return modelMatrix
            .identity()
            .translate(position.x, position.y, 0)
            .rotateZ((float) toRadians(-rotation))
            .scaleXY(scaleX, scaleY);
   }
}
