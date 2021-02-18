package com.bartlomiejpluta.base.game.map.layer.color;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.util.mesh.MeshManager;
import com.bartlomiejpluta.base.core.world.camera.Camera;
import com.bartlomiejpluta.base.core.world.object.Sprite;
import com.bartlomiejpluta.base.game.map.layer.base.Layer;
import com.bartlomiejpluta.base.game.map.model.GameMap;
import lombok.NonNull;

public class ColorLayer extends Sprite implements Layer {
   private final float mapWidth;
   private final float mapHeight;

   public ColorLayer(@NonNull MeshManager meshManager, @NonNull GameMap map, float r, float g, float b, float alpha) {
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
