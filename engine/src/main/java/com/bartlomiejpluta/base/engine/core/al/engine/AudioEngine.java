package com.bartlomiejpluta.base.engine.core.al.engine;

import com.bartlomiejpluta.base.engine.common.init.Initializable;
import com.bartlomiejpluta.base.engine.core.al.listener.AudioListener;
import com.bartlomiejpluta.base.engine.core.al.source.AudioSource;
import com.bartlomiejpluta.base.internal.gc.Cleanable;

import java.nio.ByteBuffer;

public interface AudioEngine extends Initializable, Cleanable {
   AudioListener getListener();

   void loadVorbis(String name, ByteBuffer vorbis);

   AudioSource createSource(String name);

   void disposeSource(AudioSource source);
}
