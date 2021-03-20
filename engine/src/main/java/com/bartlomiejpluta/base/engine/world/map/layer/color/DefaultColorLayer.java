package com.bartlomiejpluta.base.engine.world.map.layer.color;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.map.layer.color.ColorLayer;
import com.bartlomiejpluta.base.api.game.map.model.GameMap;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.object.Placeable;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.util.mesh.MeshManager;
import com.bartlomiejpluta.base.engine.world.map.layer.base.BaseLayer;
import com.bartlomiejpluta.base.engine.world.object.Sprite;
import lombok.NonNull;
import org.joml.Matrix4fc;
import org.joml.Vector2fc;

public class DefaultColorLayer extends BaseLayer implements ColorLayer {
   private final Color color;
   private final Material material;

   public DefaultColorLayer(@NonNull GameMap map, @NonNull MeshManager meshManager, float red, float green, float blue, float alpha) {
      super(map);
      this.color = new Color(meshManager, red, green, blue, alpha);
      this.material = color.getMaterial();

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
   public Vector2fc getPosition() {
      return color.getPosition();
   }

   @Override
   public void setPosition(float x, float y) {
      color.setPosition(x, y);
   }

   @Override
   public void setPosition(Vector2fc position) {
      color.setPosition(position);
   }

   @Override
   public void movePosition(float x, float y) {
      color.movePosition(x, y);
   }

   @Override
   public void movePosition(Vector2fc position) {
      color.movePosition(position);
   }

   @Override
   public float getRotation() {
      return color.getRotation();
   }

   @Override
   public void setRotation(float rotation) {
      color.setRotation(rotation);
   }

   @Override
   public void moveRotation(float rotation) {
      color.moveRotation(rotation);
   }

   @Override
   public float getScaleX() {
      return color.getScaleX();
   }

   @Override
   public void setScaleX(float scale) {
      color.setScaleX(scale);
   }

   @Override
   public float getScaleY() {
      return color.getScaleY();
   }

   @Override
   public void setScaleY(float scale) {
      color.setScaleY(scale);
   }

   @Override
   public void setScale(float scale) {
      color.setScale(scale);
   }

   @Override
   public void setScale(float scaleX, float scaleY) {
      color.setScale(scaleX, scaleY);
   }

   @Override
   public float euclideanDistance(Placeable other) {
      return color.euclideanDistance(other);
   }

   @Override
   public Matrix4fc getModelMatrix() {
      return color.getModelMatrix();
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      color.render(screen, camera, shaderManager);
      super.render(screen, camera, shaderManager);
   }

   private static class Color extends Sprite {
      public Color(@NonNull MeshManager meshManager, float red, float green, float blue, float alpha) {
         super(meshManager.createQuad(1, 1, 0, 0), Material.colored(red, green, blue, alpha));
      }
   }
}
