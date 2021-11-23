package com.bartlomiejpluta.base.editor.common.logs.style

import javafx.scene.paint.Color
import javafx.scene.text.Text

enum class LogsPaneStyle(private val color: Color?) {
   DEFAULT(null),
   ERROR(Color.RED);

   fun apply(text: Text) {
      text.fill = color ?: text.fill
   }
}