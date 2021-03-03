package com.bartlomiejpluta.base.api.game.camera;

import com.bartlomiejpluta.base.api.game.window.Window;
import com.bartlomiejpluta.base.api.internal.object.Placeable;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import org.joml.Matrix4f;

public interface Camera extends Placeable {
   Matrix4f computeViewModelMatrix(Matrix4f modelMatrix);

   void render(Window window, ShaderManager shaderManager);
}
