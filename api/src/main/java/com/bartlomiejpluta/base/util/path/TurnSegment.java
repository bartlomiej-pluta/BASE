package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;
import lombok.NonNull;

public class TurnSegment<T extends Character> implements PathSegment<T> {
   private final Direction direction;
   private final Integer newAnimationFrame;

   public TurnSegment(@NonNull Direction direction, int newAnimationFrame) {
      this.direction = direction;
      this.newAnimationFrame = newAnimationFrame;
   }

   public TurnSegment(@NonNull Direction direction) {
      this.direction = direction;
      this.newAnimationFrame = null;
   }

   @Override
   public PathProgress perform(T character, ObjectLayer layer, float dt) {
      character.setFaceDirection(direction);
      if (newAnimationFrame != null) {
         character.setAnimationFrame(newAnimationFrame);
      }
      return PathProgress.SEGMENT_DONE;
   }
}
