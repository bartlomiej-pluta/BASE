package com.bartlomiejpluta.base.game.world;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.world.animation.AnimationableObject;
import com.bartlomiejpluta.base.game.logic.Updatable;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;
import org.joml.Vector2i;

@Slf4j
public abstract class MoveableObject extends AnimationableObject implements Updatable {
   private static final Vector2i SPRITE_DIMENSION = new Vector2i(4, 4);
   private static final float tolerance = 1f;

   private final Vector2i coordinates = new Vector2i(0, 0);
   private final Vector2f coordinateStepSize;
   private Vector2i targetCoordinates;
   private Vector2f targetPosition;
   private Vector2f targetOffset;
   private Vector2f dS;

   @Getter
   @Setter
   private Direction faceDirection = Direction.DOWN;

   @Getter
   @Setter
   private Direction movementDirection = Direction.DOWN;

   @Getter
   @Setter
   private float speed;

   @Override
   public int getAnimationSpeed() {
      return 100;
   }

   @Override
   public boolean shouldAnimate() {
      return targetCoordinates != null;
   }

   @Override
   public void update(float dt) {
      if(targetCoordinates != null) {
         movePosition(dS);
         var distance = new Vector2f(dS).add(getPosition()).distance(targetPosition);

         log.info("Target: [{}, {}], Current: [{}, {}], distance: {}, dS: [{}, {}]", targetPosition.x, targetPosition.y, getPosition().x, getPosition().y, distance, dS.x, dS.y);

         if(distance < tolerance) {
            setCoordinates(new Vector2i(coordinates).add(targetCoordinates));
            targetCoordinates = null;
         }
      }
   }

   public void move(Direction direction) {
      if(this.targetCoordinates != null) {
         return;
      }

      setFaceDirection(direction);
      targetCoordinates = switch (direction) {
         case UP -> new Vector2i(0, -1);
         case DOWN -> new Vector2i(0, 1);
         case LEFT -> new Vector2i(-1, 0);
         case RIGHT -> new Vector2i(1, 0);
      };

      targetOffset = new Vector2f(targetCoordinates).mul(coordinateStepSize);
      targetPosition = new Vector2f(getPosition()).add(targetOffset);
      dS = new Vector2f(targetOffset).mul(speed);
      log.info("TargetCoord: [{}, {}], targetOffset: [{}, {}], dS: [{}, {}], targetPos: [{}, {}]", targetCoordinates.x, targetCoordinates.y, targetOffset.x, targetOffset.y, dS.x, dS.y, targetPosition.x, targetPosition.y);

      this.movementDirection = direction;
   }

   public MoveableObject setCoordinates(int x, int y) {
      coordinates.x = x;
      coordinates.y = y;
      setPosition((x + 0.5f) * coordinateStepSize.x, (y + 0.5f) * coordinateStepSize.y);
      return this;
   }

   public MoveableObject setCoordinates(Vector2i coordinates) {
      return setCoordinates(coordinates.x, coordinates.y);
   }

   public MoveableObject moveCoordinates(int x, int y) {
      coordinates.x += x;
      coordinates.y += y;
      movePosition(x * coordinateStepSize.x, y * coordinateStepSize.y);
      return this;
   }

   public MoveableObject moveCoordinates(Vector2i coordinates) {
      return moveCoordinates(coordinates.x, coordinates.y);
   }

   @Override
   public Vector2i getSpriteSheetDimensions() {
      return SPRITE_DIMENSION;
   }

   @Override
   public Vector2f[] getSpriteAnimationFramesPositions() {
      var row = switch (faceDirection) {
         case DOWN -> 0;
         case LEFT -> 1;
         case RIGHT -> 2;
         case UP -> 3;
      };

      return new Vector2f[]{new Vector2f(0, row), new Vector2f(1, row), new Vector2f(2, row), new Vector2f(3, row)};
   }

   public MoveableObject(Material material, Vector2f coordinateStepSize, float scale) {
      super(buildMesh(material), material);
      this.coordinateStepSize = coordinateStepSize;
      this.setScale(scale);
      setCoordinates(0, 0);
   }

   private static Mesh buildMesh(Material material) {
      var texture = material.getTexture();
      var spriteWidth = texture.getWidth() / (float) SPRITE_DIMENSION.x;
      var spriteHeight = texture.getHeight() / (float) SPRITE_DIMENSION.y;
      return Mesh.quad(spriteWidth, spriteHeight, spriteWidth / 2, spriteHeight);
   }
}
