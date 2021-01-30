package com.bartlomiejpluta.samplegame.core.world.object;

import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static java.lang.Math.toRadians;

public abstract class Object {
   private final Matrix4f modelMatrix = new Matrix4f();

   @Getter
   protected final Vector3f position = new Vector3f(0, 0, 0);

   @Getter
   protected final Vector3f rotation = new Vector3f(0, 0, 0);

   @Getter
   @Setter
   protected float scale = 1.0f;

   public Object setPosition(float x, float y, float z) {
      position.x = x;
      position.y = y;
      position.z = z;
      return this;
   }

   public Object setPosition(Vector3f position) {
      this.position.x = position.x;
      this.position.y = position.y;
      this.position.z = position.z;
      return this;
   }
   
   public Object movePosition(float x, float y, float z) {
      position.x += x;
      position.y += y;
      position.z += z;
      return this;
   }

   public Object movePosition(Vector3f position) {
      this.position.x += position.x;
      this.position.y += position.y;
      this.position.z += position.z;
      return this;
   }

   public Object setRotation(float x, float y, float z) {
      rotation.x = x;
      rotation.y = y;
      rotation.z = z;
      return this;
   }

   public Object setRotation(Vector3f rotation) {
      this.rotation.x = rotation.x;
      this.rotation.y = rotation.y;
      this.rotation.z = rotation.z;
      return this;
   }

   public Object moveRotation(float x, float y, float z) {
      rotation.x += x;
      rotation.y += y;
      rotation.z += z;
      return this;
   }

   public Object moveRotation(Vector3f rotation) {
      this.rotation.x += rotation.x;
      this.rotation.y += rotation.y;
      this.rotation.z += rotation.z;
      return this;
   }
   
   public Matrix4f getModelMatrix() {
      return modelMatrix
              .identity()
              .translate(position)
              .rotateX((float) toRadians(-rotation.x))
              .rotateY((float) toRadians(-rotation.y))
              .rotateZ((float) toRadians(-rotation.z))
              .scale(scale);
   }
}
