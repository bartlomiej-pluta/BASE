package com.bartlomiejpluta.base.engine.gui.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;

@Getter
@RequiredArgsConstructor
public class Font {
   private final String name;
   private final ByteBuffer byteBuffer;
}
