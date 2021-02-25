package com.bartlomiejpluta.base.editor.code.build.game

import org.springframework.stereotype.Component
import java.io.File

@Component
class DefaultGameEngineProvider : GameEngineProvider {

   override fun provideBaseGameEngine(target: File) {
      javaClass.getResourceAsStream("/engine/game.jar").use { ris ->
         target.outputStream().use { fos ->
            ris.copyTo(fos)
         }
      }
   }
}