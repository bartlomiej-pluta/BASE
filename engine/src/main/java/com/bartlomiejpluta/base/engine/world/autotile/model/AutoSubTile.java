package com.bartlomiejpluta.base.engine.world.autotile.model;

import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.world.object.Sprite;
import lombok.Getter;
import lombok.NonNull;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2ic;

@Getter
public class AutoSubTile extends Sprite {
   private final Vector2fc tileSpriteSize;
   private final Vector2f tileScale = new Vector2f(1, 1);

   public AutoSubTile(Mesh mesh, AutoTileSet autoTileSet) {
      super(mesh, Material.textured(autoTileSet.getTexture()));

      tileSpriteSize = material.getTexture().getSpriteSize();

      super.setScale(tileSpriteSize.x() * tileScale.x, tileSpriteSize.y() * tileScale.y);
   }

   public void recalculate(@NonNull Vector2ic spritePosition) {
      material.setSpritePosition(spritePosition.y(), spritePosition.x());
   }

   @Override
   public void setScale(float scale) {
      this.tileScale.x = scale;
      this.tileScale.y = scale;
      super.setScale(tileSpriteSize.x() * scale, tileSpriteSize.y() * scale);
   }

   public void setScale(float scaleX, float scaleY) {
      this.tileScale.x = scaleX;
      this.tileScale.y = scaleY;
      super.setScale(tileSpriteSize.x() * scaleX, tileSpriteSize.y() * scaleY);
   }

   @Override
   public float getScaleX() {
      return tileScale.x;
   }

   @Override
   public void setScaleX(float scaleX) {
      this.tileScale.x = scaleX;
      super.setScaleX(tileSpriteSize.x() * scaleX);
   }

   @Override
   public float getScaleY() {
      return tileScale.y;
   }

   @Override
   public void setScaleY(float scaleY) {
      this.tileScale.y = scaleY;
      super.setScaleY(tileSpriteSize.x() * scaleY);
   }
}
