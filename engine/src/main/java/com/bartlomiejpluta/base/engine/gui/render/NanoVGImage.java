package com.bartlomiejpluta.base.engine.gui.render;

import com.bartlomiejpluta.base.api.game.gui.base.Image;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class NanoVGImage implements Image {
   private final int imageHandle;
   private final int width;
   private final int height;
}
