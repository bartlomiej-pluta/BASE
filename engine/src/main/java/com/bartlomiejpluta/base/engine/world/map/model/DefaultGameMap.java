package com.bartlomiejpluta.base.engine.world.map.model;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.entity.Movement;
import com.bartlomiejpluta.base.api.game.map.GameMap;
import com.bartlomiejpluta.base.api.game.map.Layer;
import com.bartlomiejpluta.base.api.game.map.PassageAbility;
import com.bartlomiejpluta.base.api.game.map.TileLayer;
import com.bartlomiejpluta.base.api.game.window.Window;
import com.bartlomiejpluta.base.api.internal.logic.Updatable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.engine.util.mesh.MeshManager;
import com.bartlomiejpluta.base.engine.world.entity.model.DefaultEntity;
import com.bartlomiejpluta.base.engine.world.image.model.Image;
import com.bartlomiejpluta.base.engine.world.map.layer.color.ColorLayer;
import com.bartlomiejpluta.base.engine.world.map.layer.image.ImageLayer;
import com.bartlomiejpluta.base.engine.world.map.layer.image.ImageLayerMode;
import com.bartlomiejpluta.base.engine.world.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.engine.world.map.layer.tile.DefaultTileLayer;
import com.bartlomiejpluta.base.engine.world.tileset.model.TileSet;
import lombok.Getter;
import lombok.NonNull;
import org.joml.Vector2f;

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
   private final Vector2f stepSize;

   @Getter
   private final String handler;

   public DefaultGameMap(TileSet tileSet, int rows, int columns, String handler) {
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

   @Override
   public Vector2f getSize() {
      return new Vector2f(columns * stepSize.x, rows * stepSize.y);
   }

   @Override
   public TileLayer getTileLayer(int layerIndex) {
      return (TileLayer) layers.get(layerIndex);
   }

   public int createObjectLayer() {
      var passageMap = new PassageAbility[rows][columns];
      for (int i = 0; i < rows; ++i) {
         Arrays.fill(passageMap[i], 0, columns, PassageAbility.ALLOW);
      }

      layers.add(new ObjectLayer(new ArrayList<>(), passageMap));

      return layers.size() - 1;
   }

   public TileLayer createTileLayer() {
      var layer = new DefaultTileLayer(tileSet, rows, columns);
      layers.add(layer);

      return layer;
   }

   public int createColorLayer(MeshManager meshManager, float r, float g, float b, float alpha) {
      layers.add(new ColorLayer(meshManager, this, r, g, b, alpha));

      return layers.size() - 1;
   }

   public int createImageLayer(Image image, float opacity, float x, float y, float scaleX, float scaleY, ImageLayerMode mode, boolean parallax) {
      layers.add(new ImageLayer(this, image, opacity, x, y, scaleX, scaleY, mode, parallax));

      return layers.size() - 1;
   }

   @Override
   public void addEntity(int objectLayerIndex, Entity entity) {
      var object = (DefaultEntity) entity;
      object.setStepSize(stepSize.x, stepSize.y);

      ((ObjectLayer) layers.get(objectLayerIndex)).addObject(object);
   }

   @Override
   public void removeEntity(int objectLayerIndex, Entity entity) {
      ((ObjectLayer) layers.get(objectLayerIndex)).removeObject(entity);
   }

   @Override
   public void setPassageAbility(int objectLayerIndex, int row, int column, PassageAbility passageAbility) {
      ((ObjectLayer) layers.get(objectLayerIndex)).setPassageAbility(row, column, passageAbility);
   }

   @Override
   public void setColor(int colorLayerIndex, float r, float g, float b, float alpha) {
      ((ColorLayer) layers.get(colorLayerIndex)).setColor(r, g, b, alpha);
   }

   @Override
   public boolean isMovementPossible(int objectLayerIndex, Movement movement) {
      var target = movement.getTo();

      // Is trying to go beyond the map
      if (target.x < 0 || target.y < 0 || target.x >= columns || target.y >= rows) {
         return false;
      }

      var source = movement.getFrom();
      var direction = movement.getDirection();

      return ((ObjectLayer) layers.get(objectLayerIndex)).isMovementPossible(source, target, direction);
   }
}
