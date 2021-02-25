package com.bartlomiejpluta.base.editor.event

import org.codehaus.commons.compiler.Location
import tornadofx.EventBus
import tornadofx.FXEvent

data class UpdateCompilationLogEvent(val severity: Severity, val message: String, val location: Location? = null) :
   FXEvent(EventBus.RunOn.ApplicationThread) {

   enum class Severity {
      INFO,
      WARNING,
      ERROR
   }
}