package com.bartlomiejpluta.base.editor.process.view

import com.bartlomiejpluta.base.editor.common.logs.component.LogsPane
import com.bartlomiejpluta.base.editor.event.ClearProcessLogsEvent
import com.bartlomiejpluta.base.editor.process.runner.app.ApplicationRunner
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*


class ProcessLogsView : View() {
   private val logsPane = LogsPane()

   private val applicationRunner: ApplicationRunner by di()

   init {
      subscribe<ClearProcessLogsEvent> {
         logsPane.clear()
      }

      applicationRunner.initStreams(logsPane.stdout, logsPane.stderr)
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