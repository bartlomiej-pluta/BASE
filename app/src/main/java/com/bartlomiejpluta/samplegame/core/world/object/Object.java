package com.bartlomiejpluta.samplegame.core.world.object;

import com.bartlomiejpluta.samplegame.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.samplegame.core.gl.render.Renderable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.joml.Vector3f;

@RequiredArgsConstructor
public abstract class Object implements Renderable {
   private final Mesh mesh;

   @Getter
   private final Vector3f position = new Vector3f(0, 0, 0);

   @Getter
   private final Vector3f rotation = new Vector3f(0, 0, 0);

   @Getter
   @Setter
   private float scale = 1.0f;

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

   @Override
   public void render() {
      mesh.render();
   }

   @Override
   public void cleanUp() {
      mesh.cleanUp();
   }
}
