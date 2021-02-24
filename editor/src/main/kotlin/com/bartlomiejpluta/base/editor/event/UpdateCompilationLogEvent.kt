package com.bartlomiejpluta.base.editor.event

import org.codehaus.commons.compiler.Location
import tornadofx.EventBus
import tornadofx.FXEvent

data class UpdateCompilationLogEvent(val message: String, val location: Location) :
   FXEvent(EventBus.RunOn.ApplicationThread)