package com.bartlomiejpluta.base.lib.icon;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.icon.Icon;
import com.bartlomiejpluta.base.api.location.Locationable;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.internal.object.Placeable;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import lombok.NonNull;
import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector2ic;

public abstract class IconDelegate implements Icon {
   protected final Icon icon;

   protected IconDelegate(@NonNull Icon icon) {
      this.icon = icon;
   }

   @Override
   public void changeIcon(int row, int column) {
      icon.changeIcon(row, column);
   }

   @Override
   public void changeIcon(String iconSetUid, int row, int column) {
      icon.changeIcon(iconSetUid, row, column);
   }

   @Override
   public String getIconSetUid() {
      return icon.getIconSetUid();
   }

   @Override
   public int getIconSetRow() {
      return icon.getIconSetRow();
   }

   @Override
   public int getIconSetColumn() {
      return icon.getIconSetColumn();
   }

   @Override
   public void setStepSize(float x, float y) {
      icon.setStepSize(x, y);
   }

   @Override
   public Vector2ic getCoordinates() {
      return icon.getCoordinates();
   }

   @Override
   public void setCoordinates(Vector2ic coordinates) {
      icon.setCoordinates(coordinates);
   }

   @Override
   public void setCoordinates(int x, int y) {
      icon.setCoordinates(x, y);
   }

   @Override
   public Vector2fc getPositionOffset() {
      return icon.getPositionOffset();
   }

   @Override
   public void setPositionOffset(Vector2fc offset) {
      icon.setPositionOffset(offset);
   }

   @Override
   public void setPositionOffset(float offsetX, float offsetY) {
      icon.setPositionOffset(offsetX, offsetY);
   }

   @Override
   public int chebyshevDistance(Locationable other) {
      return icon.chebyshevDistance(other);
   }

   @Override
   public int manhattanDistance(Locationable other) {
      return icon.manhattanDistance(other);
   }

   @Override
   public Direction getDirectionTowards(Locationable target) {
      return icon.getDirectionTowards(target);
   }

   @Override
   public Vector2fc getPosition() {
      return icon.getPosition();
   }

   @Override
   public void setPosition(Vector2fc position) {
      icon.setPosition(position);
   }

   @Override
   public void setPosition(float x, float y) {
      icon.setPosition(x, y);
   }

   @Override
   public void movePosition(float x, float y) {
      icon.movePosition(x, y);
   }

   @Override
   public void movePosition(Vector2fc position) {
      icon.movePosition(position);
   }

   @Override
   public float getRotation() {
      return icon.getRotation();
   }

   @Override
   public void setRotation(float rotation) {
      icon.setRotation(rotation);
   }

   @Override
   public void moveRotation(float rotation) {
      icon.moveRotation(rotation);
   }

   @Override
   public float getScaleX() {
      return icon.getScaleX();
   }

   @Override
   public void setScaleX(float scale) {
      icon.setScaleX(scale);
   }

   @Override
   public float getScaleY() {
      return icon.getScaleY();
   }

   @Override
   public void setScaleY(float scale) {
      icon.setScaleY(scale);
   }

   @Override
   public void setScale(float scale) {
      icon.setScale(scale);
   }

   @Override
   public void setScale(float scaleX, float scaleY) {
      icon.setScale(scaleX, scaleY);
   }

   @Override
   public float euclideanDistance(Placeable other) {
      return icon.euclideanDistance(other);
   }

   @Override
   public int chebyshevDistance(Vector2ic coordinates) {
      return icon.chebyshevDistance(coordinates);
   }

   @Override
   public int manhattanDistance(Vector2ic coordinates) {
      return icon.manhattanDistance(coordinates);
   }

   @Override
   public Matrix4fc getModelMatrix() {
      return icon.getModelMatrix();
   }

   @Override
   public ObjectLayer getLayer() {
      return icon.getLayer();
   }

   @Override
   public void onAdd(ObjectLayer layer) {
      icon.onAdd(layer);
   }

   @Override
   public void onRemove(ObjectLayer layer) {
      icon.onRemove(layer);
   }

   @Override
   public boolean isBlocking() {
      return icon.isBlocking();
   }

   @Override
   public void setBlocking(boolean blocking) {
      icon.setBlocking(blocking);
   }

   @Override
   public int getZIndex() {
      return icon.getZIndex();
   }

   @Override
   public void setZIndex(int zIndex) {
      icon.setZIndex(zIndex);
   }

   @Override
   public <E extends Event> void handleEvent(E event) {
      icon.handleEvent(event);
   }

   @Override
   public void update(float dt) {
      icon.update(dt);
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      icon.render(screen, camera, shaderManager);
   }
}
