package com.bartlomiejpluta.samplegame.game.world.tileset.model;

import com.bartlomiejpluta.samplegame.core.gl.object.texture.Texture;

import com.bartlomiejpluta.samplegame.game.sprite.BaseSprite;
import lombok.Getter;

@Getter
public class Tile extends BaseSprite {
   private final int size;

   public Tile(Texture texture, int row, int col, int size) {
      super(texture, row, col, size, size);
      this.size = size;
   }
}
