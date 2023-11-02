package com.bartlomiejpluta.base.lib.character;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.event.EventType;
import com.bartlomiejpluta.base.api.location.Locationable;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.CharacterMovement;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.move.Movement;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.internal.object.Placeable;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector2ic;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class CharacterDelegate implements Character {
   protected final Character character;

   protected CharacterDelegate(Character character) {
      this.character = character;
   }

   @Override
   public void setStepSize(float x, float y) {
      character.setStepSize(x, y);
   }

   @Override
   public Vector2ic getCoordinates() {
      return character.getCoordinates();
   }

   @Override
   public void setCoordinates(Vector2ic coordinates) {
      character.setCoordinates(coordinates);
   }

   @Override
   public void setCoordinates(int x, int y) {
      character.setCoordinates(x, y);
   }

   @Override
   public Movement prepareMovement(Direction direction) {
      return new CharacterMovement(this, direction);
   }

   @Override
   public Movement getMovement() {
      return character.getMovement();
   }

   @Override
   public Movement move(Direction direction) {
      var movement = prepareMovement(direction);
      getLayer().pushMovement(movement);
      return movement;
   }

   @Override
   public Direction getFaceDirection() {
      return character.getFaceDirection();
   }

   @Override
   public void setFaceDirection(Direction direction) {
      character.setFaceDirection(direction);
   }

   @Override
   public void setSpeed(float speed) {
      character.setSpeed(speed);
   }

   @Override
   public boolean isAnimationEnabled() {
      return character.isAnimationEnabled();
   }

   @Override
   public void setAnimationEnabled(boolean enabled) {
      character.setAnimationEnabled(enabled);
   }

   @Override
   public void enableAnimation() {
      character.enableAnimation();
   }

   @Override
   public void disableAnimation() {
      character.disableAnimation();
   }

   @Override
   public void toggleAnimationEnabled() {
      character.toggleAnimationEnabled();
   }

   @Override
   public void setAnimationFrame(int frame) {
      character.setAnimationFrame(frame);
   }

   @Override
   public float getAnimationSpeed() {
      return character.getAnimationSpeed();
   }

   @Override
   public void setAnimationSpeed(float speed) {
      character.setAnimationSpeed(speed);
   }

   @Override
   public boolean isMoving() {
      return character.isMoving();
   }

   @Override
   public Vector2fc getPositionOffset() {
      return character.getPositionOffset();
   }

   @Override
   public void setPositionOffset(Vector2fc offset) {
      character.setPositionOffset(offset);
   }

   @Override
   public void setPositionOffset(float offsetX, float offsetY) {
      character.setPositionOffset(offsetX, offsetY);
   }

   @Override
   public int chebyshevDistance(Locationable other) {
      return character.chebyshevDistance(other);
   }

   @Override
   public int manhattanDistance(Locationable other) {
      return character.manhattanDistance(other);
   }

   @Override
   public double euclideanDistance(Locationable other) {
      return character.euclideanDistance(other);
   }

   @Override
   public Direction getDirectionTowards(Locationable target) {
      return character.getDirectionTowards(target);
   }

   @Override
   public Vector2fc getPosition() {
      return character.getPosition();
   }

   @Override
   public void setPosition(Vector2fc position) {
      character.setPosition(position);
   }

   @Override
   public void setPosition(float x, float y) {
      character.setPosition(x, y);
   }

   @Override
   public void movePosition(float x, float y) {
      character.movePosition(x, y);
   }

   @Override
   public void movePosition(Vector2fc position) {
      character.movePosition(position);
   }

   @Override
   public float getRotation() {
      return character.getRotation();
   }

   @Override
   public void setRotation(float rotation) {
      character.setRotation(rotation);
   }

   @Override
   public void moveRotation(float rotation) {
      character.moveRotation(rotation);
   }

   @Override
   public float getScaleX() {
      return character.getScaleX();
   }

   @Override
   public void setScaleX(float scale) {
      character.setScaleX(scale);
   }

   @Override
   public float getScaleY() {
      return character.getScaleY();
   }

   @Override
   public void setScaleY(float scale) {
      character.setScaleY(scale);
   }

   @Override
   public void setScale(float scale) {
      character.setScale(scale);
   }

   @Override
   public void setScale(float scaleX, float scaleY) {
      character.setScale(scaleX, scaleY);
   }

   @Override
   public float euclideanDistance(Placeable other) {
      return character.euclideanDistance(other);
   }

   @Override
   public double euclideanDistance(Vector2ic coordinates) {
      return character.euclideanDistance(coordinates);
   }

   @Override
   public int chebyshevDistance(Vector2ic coordinates) {
      return character.chebyshevDistance(coordinates);
   }

   @Override
   public int manhattanDistance(Vector2ic coordinates) {
      return character.manhattanDistance(coordinates);
   }

   @Override
   public Matrix4fc getModelMatrix() {
      return character.getModelMatrix();
   }

   @Override
   public ObjectLayer getLayer() {
      return character.getLayer();
   }

   @Override
   public void onAdd(ObjectLayer layer) {
      character.onAdd(layer);
   }

   @Override
   public void onRemove(ObjectLayer layer) {
      character.onRemove(layer);
   }

   @Override
   public boolean isBlocking() {
      return character.isBlocking();
   }

   @Override
   public void setBlocking(boolean blocking) {
      character.setBlocking(blocking);
   }

   @Override
   public void changeCharacterSet(String characterSetUid) {
      character.changeCharacterSet(characterSetUid);
   }

   @Override
   public int getZIndex() {
      return character.getZIndex();
   }

   @Override
   public void setZIndex(int zIndex) {
      character.setZIndex(zIndex);
   }

   @Override
   public <E extends Event> void handleEvent(E event) {
      character.handleEvent(event);
   }

   @Override
   public <E extends Event> void addEventListener(EventType<E> type, Consumer<E> listener) {
      character.addEventListener(type, listener);
   }

   @Override
   public <E extends Event> void removeEventListener(EventType<E> type, Consumer<E> listener) {
      character.removeEventListener(type, listener);
   }

   @Override
   public CompletableFuture<Void> performInstantAnimation() {
      return character.performInstantAnimation();
   }

   @Override
   public boolean move(Movement movement) {
      return character.move(movement);
   }

   @Override
   public void abortMove() {
      character.abortMove();
   }

   @Override
   public void update(float dt) {
      character.update(dt);
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      character.render(screen, camera, shaderManager);
   }
}
