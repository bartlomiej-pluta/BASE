package com.bartlomiejpluta.base.editor.code.build.pipeline

import javafx.beans.property.BooleanProperty
import javafx.concurrent.Task

interface BuildPipelineService {
   val isRunningProperty: BooleanProperty
   fun build(): Task<Unit>
   fun clean()
}