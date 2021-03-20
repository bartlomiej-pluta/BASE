package com.bartlomiejpluta.base.api.game.animation;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.object.Placeable;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector2ic;

public abstract class AnimationDelegate implements Animation {
   private final Animation animation;

   public AnimationDelegate(Animation animation) {
      this.animation = animation;
   }

   @Override
   public void setStepSize(float x, float y) {
      animation.setStepSize(x, y);
   }

   @Override
   public Vector2ic getCoordinates() {
      return animation.getCoordinates();
   }

   @Override
   public void setCoordinates(Vector2ic coordinates) {
      animation.setCoordinates(coordinates);
   }

   @Override
   public void setCoordinates(int x, int y) {
      animation.setCoordinates(x, y);
   }

   @Override
   public void setAnimationSpeed(float speed) {
      animation.setAnimationSpeed(speed);
   }

   @Override
   public Integer getRepeat() {
      return animation.getRepeat();
   }

   @Override
   public void setRepeat(Integer repeat) {
      animation.setRepeat(repeat);
   }

   @Override
   public boolean finished() {
      return animation.finished();
   }

   @Override
   public void update(float dt) {
      animation.update(dt);
   }

   @Override
   public Vector2fc getPosition() {
      return animation.getPosition();
   }

   @Override
   public void setPosition(float x, float y) {
      animation.setPosition(x, y);
   }

   @Override
   public void setPosition(Vector2fc position) {
      animation.setPosition(position);
   }

   @Override
   public void movePosition(float x, float y) {
      animation.movePosition(x, y);
   }

   @Override
   public void movePosition(Vector2fc position) {
      animation.movePosition(position);
   }

   @Override
   public float getRotation() {
      return animation.getRotation();
   }

   @Override
   public void setRotation(float rotation) {
      animation.setRotation(rotation);
   }

   @Override
   public void moveRotation(float rotation) {
      animation.moveRotation(rotation);
   }

   @Override
   public float getScaleX() {
      return animation.getScaleX();
   }

   @Override
   public void setScaleX(float scale) {
      animation.setScaleX(scale);
   }

   @Override
   public float getScaleY() {
      return animation.getScaleY();
   }

   @Override
   public void setScaleY(float scale) {
      animation.setScaleY(scale);
   }

   @Override
   public void setScale(float scale) {
      animation.setScale(scale);
   }

   @Override
   public void setScale(float scaleX, float scaleY) {
      animation.setScale(scaleX, scaleY);
   }

   @Override
   public float euclideanDistance(Placeable other) {
      return animation.euclideanDistance(other);
   }

   @Override
   public Matrix4fc getModelMatrix() {
      return animation.getModelMatrix();
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      animation.render(screen, camera, shaderManager);
   }
}
