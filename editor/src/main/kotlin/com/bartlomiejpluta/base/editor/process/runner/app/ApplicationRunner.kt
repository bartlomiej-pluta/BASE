package com.bartlomiejpluta.base.editor.process.runner.app

import javafx.beans.binding.BooleanExpression
import javafx.beans.property.ObjectProperty
import java.io.OutputStream

interface ApplicationRunner {
   fun initStreams(stdout: OutputStream, stderr: OutputStream)
   val isRunningProperty: BooleanExpression
   val processProperty: ObjectProperty<Process>
   fun run()
   fun terminate()
}