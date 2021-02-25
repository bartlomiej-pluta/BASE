package com.bartlomiejpluta.base.editor.code.build.game

import org.springframework.stereotype.Component
import java.io.File

@Component
class DefaultGameEngineProvider : GameEngineProvider {

   override fun provideBaseGameEngine(targetJar: File) {
      javaClass.getResourceAsStream("/engine/game.jar").use { ris ->
         targetJar.outputStream().use { fos ->
            ris.copyTo(fos)
         }
      }
   }
}