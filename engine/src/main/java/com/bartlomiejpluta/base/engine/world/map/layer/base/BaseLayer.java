package com.bartlomiejpluta.base.engine.world.map.layer.base;

import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.internal.logic.Updatable;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import lombok.NonNull;
import org.joml.Vector2fc;

import java.util.ArrayList;

public abstract class BaseLayer implements Layer, Updatable {

   @NonNull
   protected final GameMap map;

   @NonNull
   protected final Vector2fc stepSize;

   protected final ArrayList<Animation> animations = new ArrayList<>();

   public BaseLayer(@NonNull GameMap map) {
      this.map = map;
      this.stepSize = map.getStepSize();
   }

   @Override
   public void pushAnimation(Animation animation) {
      animations.add(animation);
      animation.setStepSize(stepSize.x(), stepSize.y());
      animation.onAdd(this);
   }

   @Override
   public GameMap getMap() {
      return map;
   }

   @Override
   public void update(float dt) {

      // Disclaimer
      // For the sake of an easy adding and removing
      // animations from the animation.update() method inside
      // the loop, the loop itself has been implemented
      // as plain old C-style for loop.
      for (int i = 0; i < animations.size(); ++i) {
         var animation = animations.get(i);
         animation.update(dt);

         if (animation.finished()) {
            animations.remove(animation);
            animation.onFinish(this);
         }
      }
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      for (var animation : animations) {
         animation.render(screen, camera, shaderManager);
      }
   }

   @Override
   public <E extends Event> void handleEvent(E event) {
      // do nothing
   }
}
