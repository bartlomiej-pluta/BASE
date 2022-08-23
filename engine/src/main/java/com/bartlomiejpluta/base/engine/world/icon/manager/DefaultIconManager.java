package com.bartlomiejpluta.base.engine.world.icon.manager;

import com.bartlomiejpluta.base.api.icon.Icon;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.util.mesh.MeshManager;
import com.bartlomiejpluta.base.engine.world.icon.model.DefaultIcon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultIconManager implements IconManager {
   private final MeshManager meshManager;
   private final IconSetManager iconSetManager;
   private Mesh mesh;

   @Override
   public void init() {
      this.mesh = meshManager.createQuad(1, 1, 0.5f, 1);
   }

   @Override
   public Icon createIcon(String iconSetUid, int row, int column) {
      return new DefaultIcon(mesh, iconSetManager, iconSetUid, row, column);
   }

   @Override
   public void cleanUp() {
      log.info("There is nothing to clean up here");
   }
}
