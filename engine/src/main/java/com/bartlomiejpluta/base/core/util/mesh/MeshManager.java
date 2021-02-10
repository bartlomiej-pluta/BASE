package com.bartlomiejpluta.base.core.util.mesh;

import com.bartlomiejpluta.base.core.gc.Cleanable;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;

public interface MeshManager extends Cleanable {
   Mesh createQuad(float width, float height, float originX, float originY);
}
