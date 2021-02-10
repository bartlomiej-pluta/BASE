package com.bartlomiejpluta.base.core.gc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultOffHeapGarbageCollector implements OffHeapGarbageCollector {
   private final List<Cleanable> cleanables;

   @Override
   public void cleanUp() {
      cleanables.stream()
              .peek(cleanable -> log.info("Performing {} cleaning", cleanable.getClass().getSimpleName()))
              .forEach(Cleanable::cleanUp);
   }
}
