package com.bartlomiejpluta.base.engine.util.mesh;

import com.bartlomiejpluta.base.api.internal.gc.Cleanable;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;

public interface MeshManager extends Cleanable {
   Mesh createQuad(float width, float height, float originX, float originY);
}
