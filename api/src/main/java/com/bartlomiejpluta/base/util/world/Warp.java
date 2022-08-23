package com.bartlomiejpluta.base.util.world;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.context.ContextHolder;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.lib.entity.EntityDelegate;
import lombok.NonNull;
import lombok.Setter;

public class Warp extends EntityDelegate {
   protected final Context context;
   protected final String mapUid;
   protected final int layer;
   protected final int x;
   protected final int y;

   @Setter
   protected Entity entity;

   public Warp(@NonNull String mapUid, int layer, int x, int y) {
      super(ContextHolder.INSTANCE.getContext().createAbstractEntity());
      this.context = ContextHolder.INSTANCE.getContext();
      this.mapUid = mapUid;
      this.layer = layer;
      this.x = x;
      this.y = y;
   }

   @Override
   public void update(float dt) {
      if (entity != null && entity.getCoordinates().equals(getCoordinates())) {
         beforeWarp();
         context.openMap(mapUid);
         context.getMap().getObjectLayer(layer).addEntity(entity);
         entity.setCoordinates(x, y);
         afterWarp();
      }
   }

   protected void beforeWarp() {
      // noop
   }

   protected void afterWarp() {
      // noop
   }
}
