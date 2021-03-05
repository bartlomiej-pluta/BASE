package com.bartlomiejpluta.base.api.game.entity;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.window.Window;
import com.bartlomiejpluta.base.api.internal.object.Placeable;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;

public abstract class EntityDelegate implements Entity {
   private final Entity entity;

   protected EntityDelegate(Entity entity) {
      this.entity = entity;
   }

   @Override
   public void setStepSize(float x, float y) {
      entity.setStepSize(x, y);
   }

   @Override
   public Vector2i getCoordinates() {
      return entity.getCoordinates();
   }

   @Override
   public void setCoordinates(Vector2i coordinates) {
      entity.setCoordinates(coordinates);
   }

   @Override
   public void setCoordinates(int x, int y) {
      entity.setCoordinates(x, y);
   }

   @Override
   public Movement prepareMovement(Direction direction) {
      return entity.prepareMovement(direction);
   }

   @Override
   public Direction getFaceDirection() {
      return entity.getFaceDirection();
   }

   @Override
   public void setFaceDirection(Direction direction) {
      entity.setFaceDirection(direction);
   }

   @Override
   public void setSpeed(float speed) {
      entity.setSpeed(speed);
   }

   @Override
   public void setAnimationSpeed(float speed) {
      entity.setAnimationSpeed(speed);
   }

   @Override
   public boolean isMoving() {
      return entity.isMoving();
   }

   @Override
   public int chebyshevDistance(Entity other) {
      return entity.chebyshevDistance(other);
   }

   @Override
   public int manhattanDistance(Entity other) {
      return entity.manhattanDistance(other);
   }

   @Override
   public Direction getDirectionTowards(Entity target) {
      return entity.getDirectionTowards(target);
   }

   @Override
   public Vector2f getPosition() {
      return entity.getPosition();
   }

   @Override
   public void setPosition(float x, float y) {
      entity.setPosition(x, y);
   }

   @Override
   public void setPosition(Vector2f position) {
      entity.setPosition(position);
   }

   @Override
   public void movePosition(float x, float y) {
      entity.movePosition(x, y);
   }

   @Override
   public void movePosition(Vector2f position) {
      entity.movePosition(position);
   }

   @Override
   public float getRotation() {
      return entity.getRotation();
   }

   @Override
   public void setRotation(float rotation) {
      entity.setRotation(rotation);
   }

   @Override
   public void moveRotation(float rotation) {
      entity.moveRotation(rotation);
   }

   @Override
   public float getScaleX() {
      return entity.getScaleX();
   }

   @Override
   public void setScaleX(float scale) {
      entity.setScaleX(scale);
   }

   @Override
   public float getScaleY() {
      return entity.getScaleY();
   }

   @Override
   public void setScaleY(float scale) {
      entity.setScaleY(scale);
   }

   @Override
   public void setScale(float scale) {
      entity.setScale(scale);
   }

   @Override
   public void setScale(float scaleX, float scaleY) {
      entity.setScale(scaleX, scaleY);
   }

   @Override
   public float euclideanDistance(Placeable other) {
      return entity.euclideanDistance(other);
   }

   @Override
   public Matrix4f getModelMatrix() {
      return entity.getModelMatrix();
   }

   @Override
   public void update(float dt) {
      entity.update(dt);
   }

   @Override
   public void render(Window window, Camera camera, ShaderManager shaderManager) {
      entity.render(window, camera, shaderManager);
   }
}
