package com.bartlomiejpluta.base.engine.world.map.layer.base;

import com.bartlomiejpluta.base.api.game.animation.Animation;
import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.map.layer.base.Layer;
import com.bartlomiejpluta.base.api.game.map.model.GameMap;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.logic.Updatable;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import lombok.NonNull;
import org.joml.Vector2fc;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class BaseLayer implements Layer, Updatable {

   @NonNull
   protected final GameMap map;

   @NonNull
   protected final Vector2fc stepSize;

   protected final Queue<Animation> animations = new LinkedList<>();

   private final List<Animation> animationsToAdd = new LinkedList<>();

   public BaseLayer(@NonNull GameMap map) {
      this.map = map;
      this.stepSize = map.getStepSize();
   }

   @Override
   public void pushAnimation(Animation animation) {
      animationsToAdd.add(animation);
   }

   @Override
   public GameMap getMap() {
      return map;
   }

   @Override
   public void update(float dt) {
      if(!animationsToAdd.isEmpty()) {
         for(var animation : animationsToAdd) {
            animation.setStepSize(stepSize.x(), stepSize.y());
            animations.add(animation);
            animation.onAdd(this);
         }

         animationsToAdd.clear();
      }

      for (var iterator = animations.iterator(); iterator.hasNext(); ) {
         var animation = iterator.next();
         animation.update(dt);

         if (animation.finished()) {
            iterator.remove();
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
}
