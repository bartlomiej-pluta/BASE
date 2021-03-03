package com.bartlomiejpluta.base.engine.world.map.layer.color;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.window.Window;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.util.mesh.MeshManager;
import com.bartlomiejpluta.base.engine.world.map.layer.base.Layer;
import com.bartlomiejpluta.base.engine.world.map.model.DefaultGameMap;
import com.bartlomiejpluta.base.engine.world.object.Sprite;
import lombok.NonNull;

public class ColorLayer extends Sprite implements Layer {
   private final float mapWidth;
   private final float mapHeight;

   public ColorLayer(@NonNull MeshManager meshManager, @NonNull DefaultGameMap map, float r, float g, float b, float alpha) {
      super(meshManager.createQuad(1, 1, 0, 0), Material.colored(r, g, b, alpha));
      this.mapWidth = map.getWidth();
      this.mapHeight = map.getHeight();
      setScale(mapWidth, mapHeight);
   }

   public void setColor(float r, float g, float b, float alpha) {
      material.setColor(r, g, b, alpha);
   }

   @Override
   public void render(Window window, Camera camera, ShaderManager shaderManager) {
      super.render(window, camera, shaderManager);
   }

   @Override
   public void update(float dt) {

   }
}
