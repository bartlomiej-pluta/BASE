package com.bartlomiejpluta.base.editor.event

import com.bartlomiejpluta.base.editor.common.logs.enumeration.Severity
import org.codehaus.commons.compiler.Location
import tornadofx.EventBus
import tornadofx.FXEvent

data class AppendCompilationLogEvent(
   val severity: Severity,
   val message: String,
   val location: Location? = null,
   val tag: String? = null
) : FXEvent(EventBus.RunOn.ApplicationThread)