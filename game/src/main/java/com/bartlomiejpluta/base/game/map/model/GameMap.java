package com.bartlomiejpluta.base.game.map.model;

import com.bartlomiejpluta.base.core.gl.render.Renderable;
import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.logic.Updatable;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.util.mesh.MeshManager;
import com.bartlomiejpluta.base.core.world.camera.Camera;
import com.bartlomiejpluta.base.game.image.model.Image;
import com.bartlomiejpluta.base.game.map.layer.base.Layer;
import com.bartlomiejpluta.base.game.map.layer.color.ColorLayer;
import com.bartlomiejpluta.base.game.map.layer.image.ImageLayer;
import com.bartlomiejpluta.base.game.map.layer.image.ImageLayerMode;
import com.bartlomiejpluta.base.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.game.map.layer.object.PassageAbility;
import com.bartlomiejpluta.base.game.map.layer.tile.TileLayer;
import com.bartlomiejpluta.base.game.movement.MovableSprite;
import com.bartlomiejpluta.base.game.movement.Movement;
import com.bartlomiejpluta.base.game.tileset.model.TileSet;
import lombok.Getter;
import lombok.NonNull;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameMap implements Renderable, Updatable {
   @Getter
   private final List<Layer> layers = new ArrayList<>();

   @NonNull
   private final TileSet tileSet;

   @Getter
   private final int rows;

   @Getter
   private final int columns;

   @Getter
   private final float width;

   @Getter
   private final float height;

   @Getter
   private final Vector2f stepSize;

   @Getter
   private final String handler;

   public GameMap(TileSet tileSet, int rows, int columns, String handler) {
      this.tileSet = tileSet;
      this.rows = rows;
      this.columns = columns;
      this.stepSize = new Vector2f(tileSet.getTileSet().getSpriteSize());
      this.width = columns * stepSize.x;
      this.height = rows * stepSize.y;
      this.handler = handler;
   }

   @Override
   public void render(Window window, Camera camera, ShaderManager shaderManager) {
      for (var layer : layers) {
         layer.render(window, camera, shaderManager);
      }
   }

   @Override
   public void update(float dt) {
      for (var layer : layers) {
         layer.update(dt);
      }
   }

   public Vector2f getSize() {
      return new Vector2f(columns * stepSize.x, rows * stepSize.y);
   }

   public int createObjectLayer() {
      var passageMap = new PassageAbility[rows][columns];
      for (int i = 0; i < rows; ++i) {
         Arrays.fill(passageMap[i], 0, columns, PassageAbility.ALLOW);
      }

      layers.add(new ObjectLayer(new ArrayList<>(), passageMap));

      return layers.size() - 1;
   }

   public int createTileLayer() {
      layers.add(new TileLayer(rows, columns));

      return layers.size() - 1;
   }

   public int createColorLayer(MeshManager meshManager, float r, float g, float b, float alpha) {
      layers.add(new ColorLayer(meshManager, this, r, g, b, alpha));

      return layers.size() - 1;
   }

   public int createImageLayer(Image image, float opacity, float x, float y, float scaleX, float scaleY, ImageLayerMode mode, boolean parallax) {
      layers.add(new ImageLayer(this, image, opacity, x, y, scaleX, scaleY, mode, parallax));

      return layers.size() - 1;
   }

   public GameMap setTile(int layerIndex, int row, int column, int tileId) {
      ((TileLayer) layers.get(layerIndex)).setTile(row, column, tileSet.tileById(tileId));

      return this;
   }

   public GameMap setTile(int layerIndex, int row, int column, int tileSetRow, int tileSetColumn) {
      ((TileLayer) layers.get(layerIndex)).setTile(row, column, tileSet.tileAt(tileSetRow, tileSetColumn));

      return this;
   }

   public GameMap clearTile(int layerIndex, int row, int column) {
      ((TileLayer) layers.get(layerIndex)).setTile(row, column, null);

      return this;
   }

   public GameMap addObject(int layerIndex, MovableSprite object) {
      ((ObjectLayer) layers.get(layerIndex)).addObject(object);

      return this;
   }

   public GameMap removeObject(int layerIndex, MovableSprite object) {
      ((ObjectLayer) layers.get(layerIndex)).removeObject(object);

      return this;
   }

   public GameMap setPassageAbility(int layerIndex, int row, int column, PassageAbility passageAbility) {
      ((ObjectLayer) layers.get(layerIndex)).setPassageAbility(row, column, passageAbility);

      return this;
   }

   public GameMap setColor(int layerIndex, float r, float g, float b, float alpha) {
      ((ColorLayer) layers.get(layerIndex)).setColor(r, g, b, alpha);

      return this;
   }

   public boolean isMovementPossible(int layerIndex, Movement movement) {
      var target = movement.getTargetCoordinate();

      // Is trying to go beyond the map
      if (target.x < 0 || target.y < 0 || target.x >= columns || target.y >= rows) {
         return false;
      }

      var source = movement.getSourceCoordinate();
      var direction = movement.getDirection();

      return ((ObjectLayer) layers.get(layerIndex)).isMovementPossible(source, target, direction);
   }
}
