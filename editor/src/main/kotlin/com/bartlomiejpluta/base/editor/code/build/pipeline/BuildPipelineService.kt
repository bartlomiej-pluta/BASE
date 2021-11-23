package com.bartlomiejpluta.base.editor.code.build.pipeline

import javafx.beans.property.BooleanProperty
import javafx.concurrent.Task
import java.io.OutputStream

interface BuildPipelineService {
   fun initStreams(stdout: OutputStream, stderr: OutputStream)
   val isRunningProperty: BooleanProperty
   fun build(): Task<Boolean>
   fun clean()
}