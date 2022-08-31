package com.bartlomiejpluta.base.util.input;

import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.base.api.input.Input;
import com.bartlomiejpluta.base.api.input.Key;
import com.bartlomiejpluta.base.api.move.Direction;

public class InputUtil {
   public static void handleBasicControl(Character character, Input input) {
      if (character.isMoving() || character.getLayer() == null) {
         return;
      }

      if (input.isKeyPressed(Key.KEY_LEFT_CONTROL)) {
         if (input.isKeyPressed(Key.KEY_DOWN)) {
            character.setFaceDirection(Direction.DOWN);
         } else if (input.isKeyPressed(Key.KEY_UP)) {
            character.setFaceDirection(Direction.UP);
         } else if (input.isKeyPressed(Key.KEY_LEFT)) {
            character.setFaceDirection(Direction.LEFT);
         } else if (input.isKeyPressed(Key.KEY_RIGHT)) {
            character.setFaceDirection(Direction.RIGHT);
         }
      } else {
         if (input.isKeyPressed(Key.KEY_DOWN)) {
            character.move(Direction.DOWN);
         } else if (input.isKeyPressed(Key.KEY_UP)) {
            character.move(Direction.UP);
         } else if (input.isKeyPressed(Key.KEY_LEFT)) {
            character.move(Direction.LEFT);
         } else if (input.isKeyPressed(Key.KEY_RIGHT)) {
            character.move(Direction.RIGHT);
         }
      }
   }
}
