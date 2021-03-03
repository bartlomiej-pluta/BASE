package com.bartlomiejpluta.base.engine.world.object;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static java.lang.Math.toRadians;

@EqualsAndHashCode
public abstract class Model {
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

   public void setPosition(float x, float y) {
      position.x = x;
      position.y = y;
   }

   public void setPosition(Vector2f position) {
      this.position.x = position.x;
      this.position.y = position.y;
   }

   public void movePosition(float x, float y) {
      position.x += x;
      position.y += y;
   }

   public void movePosition(Vector2f position) {
      this.position.x += position.x;
      this.position.y += position.y;
   }

   public void moveRotation(float rotation) {
      this.rotation += rotation;
   }

   public void setScale(float scale) {
      this.scaleX = scale;
      this.scaleY = scale;
   }

   public void setScale(float scaleX, float scaleY) {
      this.scaleX = scaleX;
      this.scaleY = scaleY;
   }

   public Matrix4f getModelMatrix() {
      return modelMatrix
            .identity()
            .translate(position.x, position.y, 0)
            .rotateZ((float) toRadians(-rotation))
            .scaleXY(scaleX, scaleY);
   }
}
