package com.bartlomiejpluta.base.editor.process.view

import com.bartlomiejpluta.base.editor.common.logs.component.LogsPane
import com.bartlomiejpluta.base.editor.event.AppendProcessLogsEvent
import com.bartlomiejpluta.base.editor.event.ClearProcessLogsEvent
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*


class ProcessLogsView : View() {
   private val buildLogs = LogsPane()
   private val followCaret = true.toProperty()

   init {
      subscribe<AppendProcessLogsEvent> { event ->
         buildLogs.appendEntry(event.message, event.severity, followCaret.value)
      }

      subscribe<ClearProcessLogsEvent> {
         buildLogs.clear()
      }
   }

   override val root = borderpane {
      left = vbox {
         button(graphic = FontIcon("fa-trash")) {
            action { buildLogs.clear() }
         }

         togglebutton {
            followCaret.bind(selectedProperty())
            graphic = FontIcon("fa-angle-double-down")
         }
      }

      center = buildLogs
   }
}