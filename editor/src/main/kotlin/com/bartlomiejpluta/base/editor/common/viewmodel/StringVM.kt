package com.bartlomiejpluta.base.editor.common.viewmodel

import tornadofx.*

class StringVM(value: String = "") : ViewModel() {
   val valueProperty = value.toProperty()
   val value by valueProperty
}