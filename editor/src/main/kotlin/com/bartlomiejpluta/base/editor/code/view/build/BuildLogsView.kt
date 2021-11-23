package com.bartlomiejpluta.base.editor.code.view.build

import com.bartlomiejpluta.base.editor.code.build.pipeline.BuildPipelineService
import com.bartlomiejpluta.base.editor.common.logs.component.LogsPane
import com.bartlomiejpluta.base.editor.event.ClearBuildLogsEvent
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class BuildLogsView : View() {
   private val pipelineService: BuildPipelineService by di()

   private val logsPane = LogsPane()

   init {
      subscribe<ClearBuildLogsEvent> {
         logsPane.clear()
      }

      pipelineService.initStreams(logsPane.stdout, logsPane.stderr)
   }

   override val root = borderpane {
      left = vbox {
         button(graphic = FontIcon("fa-trash")) {
            action { logsPane.clear() }
         }
      }

      center = logsPane
   }
}