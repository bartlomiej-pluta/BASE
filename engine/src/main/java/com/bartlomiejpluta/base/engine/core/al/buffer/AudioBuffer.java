package com.bartlomiejpluta.base.engine.core.al.buffer;

import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.internal.gc.Disposable;
import lombok.Getter;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class AudioBuffer implements Disposable {
   private final ShortBuffer pcm;

   @Getter
   private final int id;

   public AudioBuffer(ByteBuffer buffer) {
      id = alGenBuffers();
      try (var info = STBVorbisInfo.malloc()) {
         pcm = readVorbis(buffer, info);
         alBufferData(id, info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm, info.sample_rate());
      }
   }

   private ShortBuffer readVorbis(ByteBuffer vorbis, STBVorbisInfo info) {
      try (MemoryStack stack = MemoryStack.stackPush()) {
         var error = stack.mallocInt(1);
         var decoder = stb_vorbis_open_memory(vorbis, error, null);

         if (decoder == NULL) {
            throw new AppException("Failed to open OGG Vorbis file: " + error.get(0));
         }

         stb_vorbis_get_info(decoder, info);

         int channels = info.channels();

         int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);

         var pcm = MemoryUtil.memAllocShort(lengthSamples);

         pcm.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
         stb_vorbis_close(decoder);

         return pcm;
      }
   }

   @Override
   public void dispose() {
      alDeleteBuffers(id);
      if (pcm != null) {
         MemoryUtil.memFree(pcm);
      }
   }
}
