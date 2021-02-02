package com.bartlomiejpluta.base.core.world.map;

import com.bartlomiejpluta.base.core.gl.render.Renderable;
import com.bartlomiejpluta.base.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.image.Image;
import com.bartlomiejpluta.base.core.logic.Updatable;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.world.animation.Animator;
import com.bartlomiejpluta.base.core.world.camera.Camera;
import com.bartlomiejpluta.base.core.world.movement.MovableObject;
import com.bartlomiejpluta.base.core.world.movement.Movement;
import com.bartlomiejpluta.base.core.world.tileset.model.Tile;
import com.bartlomiejpluta.base.core.world.tileset.model.TileSet;
import lombok.Getter;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameMap implements Renderable, Updatable {
   private final Animator animator;

   private final Camera camera;
   private final TileSet tileSet;
   private final List<Layer> layers = new ArrayList<>();

   private final float scale;

   @Getter
   private final int rows;

   @Getter
   private final int columns;

   @Getter
   private final Vector2f stepSize;

   public GameMap(Animator animator, Camera camera, TileSet tileSet, int rows, int columns, float scale) {
      this.animator = animator;
      this.camera = camera;
      this.tileSet = tileSet;
      this.scale = scale;
      this.rows = rows;
      this.columns = columns;
      this.stepSize = new Vector2f(this.scale * this.tileSet.getTileWidth(), this.scale * this.tileSet.getTileHeight());
   }

   @Override
   public void render(Window window, ShaderManager shaderManager) {
      shaderManager.setUniform(UniformName.UNI_PROJECTION_MATRIX, camera.getProjectionMatrix(window));
      shaderManager.setUniform(UniformName.UNI_VIEW_MATRIX, camera.getViewMatrix());

      for (var layer : layers) {
         layer.render(window, shaderManager);
      }
   }

   @Override
   public void update(float dt) {
      for (var layer : layers) {
         layer.update(dt);
      }
   }

   public GameMap createObjectLayer() {
      var passageMap = new PassageAbility[rows][columns];
      for (int i = 0; i < rows; ++i) {
         Arrays.fill(passageMap[i], 0, columns, PassageAbility.ALLOW);
      }

      layers.add(new ObjectLayer(animator, new ArrayList<>(), passageMap));

      return this;
   }

   public GameMap createTileLayer() {
      layers.add(new TileLayer(new Tile[rows][columns], stepSize, scale));

      return this;
   }

   public GameMap createImageLayer(Image image, ImageLayer.Mode imageDisplayMode) {
      layers.add(new ImageLayer(this, image, imageDisplayMode));

      return this;
   }

   public GameMap addObject(int layerIndex, MovableObject object) {
      ((ObjectLayer) layers.get(layerIndex)).addObject(object);

      return this;
   }

   public GameMap removeObject(int layerIndex, MovableObject object) {
      ((ObjectLayer) layers.get(layerIndex)).removeObject(object);

      return this;
   }

   public GameMap setPassageAbility(int layerIndex, int row, int column, PassageAbility passageAbility) {
      ((ObjectLayer) layers.get(layerIndex)).setPassageAbility(row, column, passageAbility);
      return this;
   }

   public GameMap setTile(int layerIndex, int row, int column, Tile tile) {
      ((TileLayer) layers.get(layerIndex)).setTile(row, column, tile);

      return this;
   }

   public GameMap setImage(int layerIndex, Image image) {
      ((ImageLayer) layers.get(layerIndex)).setImage(image);

      return this;
   }

   public boolean isMovementPossible(int layerIndex, Movement movement) {
      var target = movement.getTargetCoordinate();

      // Is trying to go beyond the map
      if(target.x < 0 || target.y < 0 || target.x >= columns || target.y >= rows) {
         return false;
      }

      var source = movement.getSourceCoordinate();
      var direction = movement.getDirection();

      return ((ObjectLayer) layers.get(layerIndex)).isMovementPossible(source, target, direction);
   }
}
