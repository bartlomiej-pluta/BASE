package com.bartlomiejpluta.base.engine.world.map.model;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.image.Image;
import com.bartlomiejpluta.base.api.game.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.game.map.layer.color.ColorLayer;
import com.bartlomiejpluta.base.api.game.map.layer.image.ImageLayer;
import com.bartlomiejpluta.base.api.game.map.layer.image.ImageLayerMode;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.game.map.layer.object.PassageAbility;
import com.bartlomiejpluta.base.api.game.map.layer.tile.TileLayer;
import com.bartlomiejpluta.base.api.game.map.model.GameMap;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.logic.Updatable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.engine.util.mesh.MeshManager;
import com.bartlomiejpluta.base.engine.world.map.layer.color.DefaultColorLayer;
import com.bartlomiejpluta.base.engine.world.map.layer.image.DefaultImageLayer;
import com.bartlomiejpluta.base.engine.world.map.layer.object.DefaultObjectLayer;
import com.bartlomiejpluta.base.engine.world.map.layer.tile.DefaultTileLayer;
import com.bartlomiejpluta.base.engine.world.tileset.model.TileSet;
import lombok.Getter;
import lombok.NonNull;
import org.joml.Vector2f;
import org.joml.Vector2fc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultGameMap implements Renderable, Updatable, GameMap {

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
   private final Vector2fc stepSize;

   @Getter
   private final Vector2fc size;

   @Getter
   private final String handler;

   public DefaultGameMap(TileSet tileSet, int rows, int columns, String handler) {
      this.tileSet = tileSet;
      this.rows = rows;
      this.columns = columns;
      this.stepSize = new Vector2f(tileSet.getTileSet().getSpriteSize());
      this.width = columns * stepSize.x();
      this.height = rows * stepSize.y();
      this.size = new Vector2f(columns * stepSize.x(), rows * stepSize.y());
      this.handler = handler;
   }

   @Override
   public void update(float dt) {
      for (var layer : layers) {
         layer.update(dt);
      }
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      for (var layer : layers) {
         layer.render(screen, camera, shaderManager);
      }
   }

   @Override
   public TileLayer getTileLayer(int layerIndex) {
      return (TileLayer) layers.get(layerIndex);
   }

   @Override
   public ImageLayer getImageLayer(int layerIndex) {
      return (ImageLayer) layers.get(layerIndex);
   }

   @Override
   public ColorLayer getColorLayer(int layerIndex) {
      return (ColorLayer) layers.get(layerIndex);
   }

   @Override
   public ObjectLayer getObjectLayer(int layerIndex) {
      return (ObjectLayer) layers.get(layerIndex);
   }

   public TileLayer createTileLayer() {
      var layer = new DefaultTileLayer(this, tileSet, rows, columns);
      layers.add(layer);

      return layer;
   }

   public ImageLayer createImageLayer(Image image, float opacity, float x, float y, float scaleX, float scaleY, ImageLayerMode mode, boolean parallax) {
      var layer = new DefaultImageLayer(this, image, opacity, x, y, scaleX, scaleY, mode, parallax);
      layers.add(layer);

      return layer;
   }

   public ColorLayer createColorLayer(MeshManager meshManager, float red, float green, float blue, float alpha) {
      var layer = new DefaultColorLayer(this, meshManager, red, green, blue, alpha);
      layers.add(layer);

      return layer;
   }

   public ObjectLayer createObjectLayer() {
      var passageMap = new PassageAbility[rows][columns];
      for (int i = 0; i < rows; ++i) {
         Arrays.fill(passageMap[i], 0, columns, PassageAbility.ALLOW);
      }

      var layer = new DefaultObjectLayer(this, rows, columns, new ArrayList<>(), passageMap);

      layers.add(layer);

      return layer;
   }
}
