package com.bartlomiejpluta.base.core.world.object;

import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static java.lang.Math.toRadians;

public abstract class Object {
   private final Matrix4f modelMatrix = new Matrix4f();

   @Getter
   protected final Vector2f position = new Vector2f(0, 0);

   @Getter
   @Setter
   protected float rotation;

   @Getter
   @Setter
   protected float scale = 1.0f;

   public Object setPosition(float x, float y) {
      position.x = x;
      position.y = y;
      return this;
   }

   public Object setPosition(Vector2f position) {
      this.position.x = position.x;
      this.position.y = position.y;
      return this;
   }
   
   public Object movePosition(float x, float y) {
      position.x += x;
      position.y += y;
      return this;
   }

   public Object movePosition(Vector2f position) {
      this.position.x += position.x;
      this.position.y += position.y;
      return this;
   }

   public Object moveRotation(float rotation) {
      this.rotation += rotation;
      return this;
   }
   
   public Matrix4f getModelMatrix() {
      return modelMatrix
              .identity()
              .translate(position.x, position.y, 0)
              .rotateZ((float) toRadians(-rotation))
              .scale(scale);
   }
}
