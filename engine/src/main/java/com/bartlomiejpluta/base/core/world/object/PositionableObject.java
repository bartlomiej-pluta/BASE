package com.bartlomiejpluta.base.core.world.object;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static java.lang.Math.toRadians;

@EqualsAndHashCode
public abstract class PositionableObject {
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

   public PositionableObject setPosition(float x, float y) {
      position.x = x;
      position.y = y;
      return this;
   }

   public PositionableObject setPosition(Vector2f position) {
      this.position.x = position.x;
      this.position.y = position.y;
      return this;
   }
   
   public PositionableObject movePosition(float x, float y) {
      position.x += x;
      position.y += y;
      return this;
   }

   public PositionableObject movePosition(Vector2f position) {
      this.position.x += position.x;
      this.position.y += position.y;
      return this;
   }

   public PositionableObject moveRotation(float rotation) {
      this.rotation += rotation;
      return this;
   }

   public PositionableObject setScale(float scale) {
      this.scaleX = scale;
      this.scaleY = scale;
      return this;
   }

   public PositionableObject setScale(float scaleX, float scaleY) {
      this.scaleX = scaleX;
      this.scaleY = scaleY;
      return this;
   }

   public Matrix4f getModelMatrix() {
      return modelMatrix
              .identity()
              .translate(position.x, position.y, 0)
              .rotateZ((float) toRadians(-rotation))
              .scaleXY(scaleX, scaleY);
   }
}
