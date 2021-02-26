package com.bartlomiejpluta.base.editor.event

import com.bartlomiejpluta.base.editor.common.logs.enumeration.Severity
import tornadofx.EventBus
import tornadofx.FXEvent

data class AppendProcessLogsEvent(val severity: Severity, val message: String) :
   FXEvent(EventBus.RunOn.ApplicationThread)