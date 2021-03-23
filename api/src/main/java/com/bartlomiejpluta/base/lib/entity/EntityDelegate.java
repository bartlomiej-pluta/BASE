package com.bartlomiejpluta.base.lib.entity;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.move.Movement;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.internal.object.Placeable;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector2ic;

public abstract class EntityDelegate implements Entity {
   protected final Entity entity;

   protected EntityDelegate(Entity entity) {
      this.entity = entity;
   }

   @Override
   public void setStepSize(float x, float y) {
      entity.setStepSize(x, y);
   }

   @Override
   public Vector2ic getCoordinates() {
      return entity.getCoordinates();
   }

   @Override
   public void setCoordinates(Vector2ic coordinates) {
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
   public Movement getMovement() {
      return entity.getMovement();
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
   public Vector2fc getPositionOffset() {
      return entity.getPositionOffset();
   }

   @Override
   public void setPositionOffset(Vector2fc offset) {
      entity.setPositionOffset(offset);
   }

   @Override
   public void setPositionOffset(float offsetX, float offsetY) {
      entity.setPositionOffset(offsetX, offsetY);
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
   public Vector2fc getPosition() {
      return entity.getPosition();
   }

   @Override
   public void setPosition(float x, float y) {
      entity.setPosition(x, y);
   }

   @Override
   public void setPosition(Vector2fc position) {
      entity.setPosition(position);
   }

   @Override
   public void movePosition(float x, float y) {
      entity.movePosition(x, y);
   }

   @Override
   public void movePosition(Vector2fc position) {
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
   public int chebyshevDistance(Vector2ic coordinates) {
      return entity.chebyshevDistance(coordinates);
   }

   @Override
   public int manhattanDistance(Vector2ic coordinates) {
      return entity.manhattanDistance(coordinates);
   }

   @Override
   public Matrix4fc getModelMatrix() {
      return entity.getModelMatrix();
   }

   @Override
   public void onAdd(ObjectLayer layer) {
      entity.onAdd(layer);
   }

   @Override
   public void onRemove(ObjectLayer layer) {
      entity.onRemove(layer);
   }

   @Override
   public boolean isBlocking() {
      return entity.isBlocking();
   }

   @Override
   public void setBlocking(boolean blocking) {
      entity.setBlocking(blocking);
   }

   @Override
   public void changeEntitySet(String entitySetUid) {
      entity.changeEntitySet(entitySetUid);
   }

   @Override
   public void update(float dt) {
      entity.update(dt);
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      entity.render(screen, camera, shaderManager);
   }
}
