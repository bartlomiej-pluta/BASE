package com.bartlomiejpluta.base.editor.process.runner.app

import javafx.beans.binding.BooleanExpression
import javafx.beans.property.ObjectProperty

interface ApplicationRunner {
   val isRunningProperty: BooleanExpression
   val processProperty: ObjectProperty<Process>
   fun run()
   fun terminate()
}