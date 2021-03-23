package com.bartlomiejpluta.base.engine.core.al.engine;

import com.bartlomiejpluta.base.engine.common.init.Initianizable;
import com.bartlomiejpluta.base.engine.core.al.listener.AudioListener;
import com.bartlomiejpluta.base.engine.core.al.source.AudioSource;
import com.bartlomiejpluta.base.internal.gc.Cleanable;

import java.nio.ByteBuffer;

public interface AudioEngine extends Initianizable, Cleanable {
   AudioListener getListener();

   void loadVorbis(String name, ByteBuffer vorbis);

   AudioSource createSource(String name);

   void disposeSource(AudioSource source);
}
