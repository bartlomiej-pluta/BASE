package com.bartlomiejpluta.base.editor.code.build.exception

import com.bartlomiejpluta.base.editor.common.logs.enumeration.Severity
import com.bartlomiejpluta.base.editor.common.logs.model.Location

class BuildException(
   val severity: Severity?,
   val tag: String?,
   val location: Location?,
   message: String?,
   override val cause: Throwable?
) : Exception() {
   constructor(severity: Severity?, tag: String?, message: String?, cause: Throwable?)
         : this(severity, tag, null, message, cause)

   constructor() : this(null, null, null, null, null)

   override val message = message ?: ""
}