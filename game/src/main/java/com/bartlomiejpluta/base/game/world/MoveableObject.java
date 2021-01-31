package com.bartlomiejpluta.base.game.world;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.world.animation.AnimationableObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;
import org.joml.Vector2i;

@Slf4j
@Getter
public abstract class MoveableObject extends AnimationableObject {
   private static final Vector2i SPRITE_DIMENSION = new Vector2i(4, 4);

   private final Vector2i coordinates = new Vector2i(0, 0);
   private Direction direction = Direction.DOWN;
   private final Vector2f coordinateStepSize;

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

   public MoveableObject setDirection(Direction direction) {
      this.direction = direction;
      return this;
   }

   @Override
   public Vector2i getSpriteSheetDimensions() {
      return SPRITE_DIMENSION;
   }

   @Override
   public Vector2f[] getSpriteAnimationFramesPositions() {
      var row = switch (direction) {
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
