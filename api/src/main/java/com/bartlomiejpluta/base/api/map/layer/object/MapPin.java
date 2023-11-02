package com.bartlomiejpluta.base.api.map.layer.object;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.joml.Vector2i;
import org.joml.Vector2ic;

@Value
@Builder
public class MapPin {
   String map;
   int layer;
   int x;
   int y;

   public Vector2ic toCoordinates() {
      return new Vector2i(x, y);
   }
}
