package com.bartlomiejpluta.base.engine.world.map.layer.base;

import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.light.Light;
import com.bartlomiejpluta.base.api.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.core.gl.shader.constant.CounterName;
import com.bartlomiejpluta.base.engine.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.internal.program.Updatable;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import lombok.Getter;
import lombok.NonNull;
import org.joml.Vector2fc;

import java.util.ArrayList;

public abstract class BaseLayer implements Layer, Updatable {

   @NonNull
   protected final GameMap map;

   @NonNull
   protected final Vector2fc stepSize;

   protected final ArrayList<Animation> animations = new ArrayList<>();

   @Getter
   protected final ArrayList<Light> lights = new ArrayList<>();

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
   public void addLight(Light light) {
      lights.add(light);
      light.setStepSize(stepSize.x(), stepSize.y());
   }

   @Override
   public void removeLight(Light light) {
      // Disclaimer
      // This is a workaround for concurrent modification exception
      // which is thrown when entity is tried to be removed
      // in the body of for-each-entity loop
      lights.remove(lights.indexOf(light));
   }

   @Override
   public void clearLights() {
      lights.clear();
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
            animation.getFuture().complete(animation);
            animation.onFinish(this);
         }
      }

      // Disclaimer as above for lights
      for (int i = 0; i < lights.size(); ++i) {
         lights.get(i).update(dt);
      }
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      for (var animation : animations) {
         animation.render(screen, camera, shaderManager);
      }

      for (var light : lights) {
         light.render(screen, camera, shaderManager);
      }

      shaderManager.setUniformCounter(UniformName.UNI_ACTIVE_LIGHTS, CounterName.LIGHT);
   }

   @Override
   public <E extends Event> void handleEvent(E event) {
      // do nothing
   }
}
