package com.bartlomiejpluta.base.api.camera;

import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.internal.object.Placeable;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import org.joml.Matrix4fc;

public interface Camera extends Placeable {
   Matrix4fc computeViewModelMatrix(Matrix4fc modelMatrix);

   void render(Screen screen, ShaderManager shaderManager);
}
