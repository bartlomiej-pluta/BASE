package com.bartlomiejpluta.base.editor.util.fx

import javafx.scene.control.TextFormatter
import javafx.util.converter.IntegerStringConverter
import java.util.function.UnaryOperator

object TextFieldUtil {
   fun integerFormatter(initialValue: Int) = TextFormatter(IntegerStringConverter(), initialValue, filter)

   private val intRegex = "-?[0-9]*".toRegex()
   private val filter = UnaryOperator<TextFormatter.Change> { if (it.controlNewText.matches(intRegex)) it else null }
}