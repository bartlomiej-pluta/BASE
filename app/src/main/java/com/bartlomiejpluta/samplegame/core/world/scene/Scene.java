package com.bartlomiejpluta.samplegame.core.world.scene;

import com.bartlomiejpluta.samplegame.core.gl.render.Renderable;

import java.util.ArrayList;
import java.util.List;

public class Scene implements Renderable {
   private final List<Renderable> renderables = new ArrayList<>();

   public Scene add(Renderable renderable) {
      renderables.add(renderable);
      return this;
   }

   @Override
   public void render() {
      for(var renderable : renderables) {
         renderable.render();
      }
   }

   @Override
   public void cleanUp() {
      renderables.forEach(Renderable::cleanUp);
   }
}
