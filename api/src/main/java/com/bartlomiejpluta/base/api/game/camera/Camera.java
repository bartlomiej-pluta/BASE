package com.bartlomiejpluta.base.api.game.camera;

import com.bartlomiejpluta.base.api.game.window.Window;
import com.bartlomiejpluta.base.api.internal.object.Placeable;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import org.joml.Matrix4fc;

public interface Camera extends Placeable {
   Matrix4fc computeViewModelMatrix(Matrix4fc modelMatrix);

   void render(Window window, ShaderManager shaderManager);
}
