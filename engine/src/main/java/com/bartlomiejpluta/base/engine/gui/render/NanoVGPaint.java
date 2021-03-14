package com.bartlomiejpluta.base.engine.gui.render;

import com.bartlomiejpluta.base.api.game.gui.base.Paint;
import com.bartlomiejpluta.base.api.internal.gc.Disposable;
import lombok.*;
import org.lwjgl.nanovg.NVGPaint;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class NanoVGPaint implements Paint, Disposable {

   @NonNull
   private final NVGPaint paint;

   @Override
   public void dispose() {
      paint.free();
   }
}
