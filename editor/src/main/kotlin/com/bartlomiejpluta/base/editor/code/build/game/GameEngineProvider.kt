package com.bartlomiejpluta.base.editor.code.build.game

import java.io.File

interface GameEngineProvider {
   fun provideBaseGameEngine(target: File)
}