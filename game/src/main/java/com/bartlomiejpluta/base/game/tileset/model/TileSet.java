package com.bartlomiejpluta.base.game.tileset.model;

import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.gl.object.texture.Texture;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TileSet {
   private final Texture tileSet;
   private final Mesh mesh;

   public Tile tileById(int id) {
      return new Tile(mesh, tileSet, id);
   }

   public Tile tileAt(int row, int column) {
      return new Tile(mesh, tileSet, row, column);
   }
}
