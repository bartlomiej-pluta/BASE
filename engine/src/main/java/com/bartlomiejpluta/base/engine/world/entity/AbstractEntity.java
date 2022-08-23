package com.bartlomiejpluta.base.engine.world.entity;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.world.location.LocationableModel;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import com.bartlomiejpluta.base.lib.event.EventHandler;
import lombok.Getter;
import lombok.Setter;

public class AbstractEntity extends LocationableModel implements Entity {
   private final EventHandler eventHandler = new EventHandler();

   @Getter
   @Setter
   private boolean blocking;

   @Getter
   @Setter
   private int zIndex;

   @Getter
   private ObjectLayer layer;

   @Override
   public void onAdd(ObjectLayer layer) {
      this.layer = layer;
   }

   @Override
   public void onRemove(ObjectLayer layer) {
      this.layer = null;
   }

   @Override
   public <E extends Event> void handleEvent(E event) {
      eventHandler.handleEvent(event);
   }

   @Override
   public void update(float dt) {
      // noop
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      // noop
   }
}
