package com.bartlomiejpluta.base.api.map.layer.object;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
public class MapPin {
   String map;
   int layer;
   int x;
   int y;
}
