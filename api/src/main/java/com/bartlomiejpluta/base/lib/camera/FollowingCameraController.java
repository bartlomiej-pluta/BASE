package com.bartlomiejpluta.base.lib.camera;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.api.screen.Screen;
import lombok.NonNull;
import org.joml.Vector2fc;

public class FollowingCameraController implements CameraController {
   private final Vector2fc screenSize;
   private final Vector2fc mapSize;
   private final Camera camera;

   private Vector2fc target;

   private FollowingCameraController(@NonNull Screen screen, @NonNull Camera camera, @NonNull GameMap map) {
      this.screenSize = screen.getSize();
      this.camera = camera;
      this.mapSize = map.getSize();
   }

   public FollowingCameraController follow(@NonNull Vector2fc target) {
      this.target = target;
      return this;
   }

   public FollowingCameraController stop() {
      this.target = null;
      return this;
   }

   @Override
   public void update() {
      if (target != null) {
         final var bottomRightConstraintX = mapSize.x() - screenSize.x() / camera.getScaleX();
         final var bottomRightConstraintY = mapSize.y() - screenSize.y() / camera.getScaleY();
         final var newCameraPosX = target.x() - screenSize.x() / (2f * camera.getScaleX());
         final var newCameraPosY = target.y() - screenSize.y() / (2f * camera.getScaleY());

         camera.setPosition(
               (newCameraPosX < bottomRightConstraintX) ? (newCameraPosX > 0 ? newCameraPosX : 0) : bottomRightConstraintX,
               (newCameraPosY < bottomRightConstraintY) ? (newCameraPosY > 0 ? newCameraPosY : 0) : bottomRightConstraintY
         );
      }
   }

   public static FollowingCameraController on(Screen screen, Camera camera, GameMap map) {
      return new FollowingCameraController(screen, camera, map);
   }
}
