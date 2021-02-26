package com.bartlomiejpluta.base.editor.code.build.pipeline

import javafx.concurrent.Task

interface BuildPipelineService {
   fun build(): Task<Unit>
}