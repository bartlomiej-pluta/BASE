package com.bartlomiejpluta.base.engine.gui.model;

import com.bartlomiejpluta.base.api.game.gui.Font;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;

@Getter
@RequiredArgsConstructor
public class DefaultFont implements Font {
   private final String name;
   private final ByteBuffer byteBuffer;
}
