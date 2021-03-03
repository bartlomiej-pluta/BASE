package com.bartlomiejpluta.base.engine.world.map.layer.color;

import com.bartlomiejpluta.base.api.game.map.layer.color.ColorLayer;
import com.bartlomiejpluta.base.api.game.map.model.GameMap;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.util.mesh.MeshManager;
import com.bartlomiejpluta.base.engine.world.object.Sprite;
import lombok.NonNull;

public class DefaultColorLayer extends Sprite implements ColorLayer {

   public DefaultColorLayer(@NonNull MeshManager meshManager, @NonNull GameMap map, float red, float green, float blue, float alpha) {
      super(meshManager.createQuad(1, 1, 0, 0), Material.colored(red, green, blue, alpha));
      setScale(map.getWidth(), map.getHeight());
   }

   @Override
   public void setColor(float red, float green, float blue, float alpha) {
      material.setColor(red, green, blue, alpha);
   }

   @Override
   public void setColor(float red, float green, float blue) {
      material.setColor(red, green, blue);
   }

   @Override
   public void setRed(float red) {
      material.setRed(red);
   }

   @Override
   public void setGreen(float green) {
      material.setGreen(green);
   }

   @Override
   public void setBlue(float blue) {
      material.setBlue(blue);
   }

   @Override
   public void setAlpha(float alpha) {
      material.setAlpha(alpha);
   }

   @Override
   public void update(float dt) {
      // Do nothing
   }
}
