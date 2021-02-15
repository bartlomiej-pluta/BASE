package com.bartlomiejpluta.base.core.world.map;

import com.bartlomiejpluta.base.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.image.Image;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.world.camera.Camera;

public class ImageLayer implements Layer {

   public enum Mode {
      NORMAL,
      FIT_SCREEN,
      FIT_MAP
   }

   private final float mapWidth;
   private final float mapHeight;

   private Image image;
   private float imageInitialWidth;
   private float imageInitialHeight;
   private final Mode mode;

   public ImageLayer(GameMap map, Image image, Mode mode) {
      var stepSize = map.getStepSize();
      this.mapWidth = map.getColumns() * stepSize.x;
      this.mapHeight = map.getRows() * stepSize.y;
      this.mode = mode;
      setImage(image);
   }

   public void setImage(Image image) {
      this.image = image;
      this.imageInitialWidth = image.getInitialWidth();
      this.imageInitialHeight = image.getInitialHeight();
   }

   @Override
   public void render(Window window, Camera camera, ShaderManager shaderManager) {
      if (image == null) {
         return;
      }

      shaderManager.setUniform(UniformName.UNI_MODEL_MATRIX, image.getModelMatrix());

      switch (mode) {
         case FIT_SCREEN -> image.setScale(window.getWidth() / imageInitialWidth, window.getHeight() / imageInitialHeight);
         case FIT_MAP -> image.setScale(mapWidth / imageInitialWidth, mapHeight / imageInitialHeight);
      }

      image.render(window, camera, shaderManager);
   }

   @Override
   public void update(float dt) {

   }
}
