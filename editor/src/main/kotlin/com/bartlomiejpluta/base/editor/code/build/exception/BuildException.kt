package com.bartlomiejpluta.base.editor.code.build.exception

import com.bartlomiejpluta.base.editor.event.AppendCompilationLogEvent
import org.codehaus.commons.compiler.Location

class BuildException(
   val severity: AppendCompilationLogEvent.Severity,
   val tag: String,
   val location: Location?,
   message: String?,
   override val cause: Throwable
) : Exception() {
   constructor(severity: AppendCompilationLogEvent.Severity, tag: String, message: String?, cause: Throwable)
         : this(severity, tag, null, message, cause)

   override val message = message ?: ""
}