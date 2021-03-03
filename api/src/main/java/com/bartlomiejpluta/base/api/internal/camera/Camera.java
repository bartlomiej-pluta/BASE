package com.bartlomiejpluta.base.api.internal.camera;

import com.bartlomiejpluta.base.api.internal.object.Placeable;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.api.internal.window.Window;
import org.joml.Matrix4f;

public interface Camera extends Placeable {
   Matrix4f computeViewModelMatrix(Matrix4f modelMatrix);

   void render(Window window, ShaderManager shaderManager);
}
