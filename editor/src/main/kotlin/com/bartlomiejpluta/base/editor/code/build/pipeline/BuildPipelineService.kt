package com.bartlomiejpluta.base.editor.code.build.pipeline

import javafx.beans.property.BooleanProperty
import javafx.concurrent.Task

interface BuildPipelineService {
   fun build(): Task<Unit>
   val isRunningProperty: BooleanProperty
}