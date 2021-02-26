package com.bartlomiejpluta.base.editor.process.runner.app

import javafx.beans.binding.BooleanExpression

interface ApplicationRunner {
   val isRunningProperty: BooleanExpression
   fun run()
}