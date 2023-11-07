package com.bartlomiejpluta.base.lib.light;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.light.Light;
import com.bartlomiejpluta.base.api.location.Locationable;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.internal.object.Placeable;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import lombok.NonNull;
import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector2ic;
import org.joml.Vector3fc;

public class LightDelegate implements Light {
   protected final Light light;

   protected LightDelegate(@NonNull Light light) {
      this.light = light;
   }

   @Override
   public void setStepSize(float x, float y) {
      light.setStepSize(x, y);
   }

   @Override
   public Vector2ic getCoordinates() {
      return light.getCoordinates();
   }

   @Override
   public void setCoordinates(Vector2ic coordinates) {
      light.setCoordinates(coordinates);
   }

   @Override
   public void setCoordinates(int x, int y) {
      light.setCoordinates(x, y);
   }

   @Override
   public Vector2fc getPositionOffset() {
      return light.getPositionOffset();
   }

   @Override
   public void setPositionOffset(Vector2fc offset) {
      light.setPositionOffset(offset);
   }

   @Override
   public void setPositionOffset(float offsetX, float offsetY) {
      light.setPositionOffset(offsetX, offsetY);
   }

   @Override
   public int chebyshevDistance(Locationable other) {
      return light.chebyshevDistance(other);
   }

   @Override
   public int manhattanDistance(Locationable other) {
      return light.manhattanDistance(other);
   }

   @Override
   public double euclideanDistance(Locationable other) {
      return light.euclideanDistance(other);
   }

   @Override
   public Direction getDirectionTowards(Locationable target) {
      return light.getDirectionTowards(target);
   }

   @Override
   public Vector2fc getPosition() {
      return light.getPosition();
   }

   @Override
   public void setPosition(Vector2fc position) {
      light.setPosition(position);
   }

   @Override
   public void setPosition(float x, float y) {
      light.setPosition(x, y);
   }

   @Override
   public void movePosition(float x, float y) {
      light.movePosition(x, y);
   }

   @Override
   public void movePosition(Vector2fc position) {
      light.movePosition(position);
   }

   @Override
   public float getRotation() {
      return light.getRotation();
   }

   @Override
   public void setRotation(float rotation) {
      light.setRotation(rotation);
   }

   @Override
   public void moveRotation(float rotation) {
      light.moveRotation(rotation);
   }

   @Override
   public float getScaleX() {
      return light.getScaleX();
   }

   @Override
   public void setScaleX(float scale) {
      light.setScaleX(scale);
   }

   @Override
   public float getScaleY() {
      return light.getScaleY();
   }

   @Override
   public void setScaleY(float scale) {
      light.setScaleY(scale);
   }

   @Override
   public void setScale(float scale) {
      light.setScale(scale);
   }

   @Override
   public void setScale(float scaleX, float scaleY) {
      light.setScale(scaleX, scaleY);
   }

   @Override
   public float euclideanDistance(Placeable other) {
      return light.euclideanDistance(other);
   }

   @Override
   public double euclideanDistance(Vector2ic coordinates) {
      return light.euclideanDistance(coordinates);
   }

   @Override
   public int chebyshevDistance(Vector2ic coordinates) {
      return light.chebyshevDistance(coordinates);
   }

   @Override
   public int manhattanDistance(Vector2ic coordinates) {
      return light.manhattanDistance(coordinates);
   }

   @Override
   public Matrix4fc getModelMatrix() {
      return light.getModelMatrix();
   }

   @Override
   public boolean isLuminescent() {
      return light.isLuminescent();
   }

   @Override
   public void setLuminescent(boolean luminescent) {
      light.setLuminescent(luminescent);
   }


   @Override
   public Vector3fc getIntensity() {
      return light.getIntensity();
   }

   @Override
   public void setIntensity(float red, float green, float blue) {
      light.setIntensity(red, green, blue);
   }

   @Override
   public float getConstantAttenuation() {
      return light.getConstantAttenuation();
   }

   @Override
   public float getLinearAttenuation() {
      return light.getLinearAttenuation();
   }

   @Override
   public float getQuadraticAttenuation() {
      return light.getQuadraticAttenuation();
   }

   @Override
   public void setConstantAttenuation(float attenuation) {
      light.setConstantAttenuation(attenuation);
   }

   @Override
   public void setLinearAttenuation(float attenuation) {
      light.setLinearAttenuation(attenuation);
   }

   @Override
   public void setQuadraticAttenuation(float attenuation) {
      light.setQuadraticAttenuation(attenuation);
   }

   @Override
   public void update(float dt) {
      light.update(dt);
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      light.render(screen, camera, shaderManager);
   }
}
