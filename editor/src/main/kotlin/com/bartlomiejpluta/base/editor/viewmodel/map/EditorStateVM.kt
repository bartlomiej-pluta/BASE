package com.bartlomiejpluta.base.editor.viewmodel.map

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.ViewModel
import tornadofx.getValue
import tornadofx.setValue

class EditorStateVM : ViewModel() {
   val selectedLayerProperty = SimpleIntegerProperty(0)
   var selectedLayer by selectedLayerProperty

   val showGridProperty = SimpleBooleanProperty(true)
   var showGrid by showGridProperty

   val coverUnderlyingLayersProperty = SimpleBooleanProperty(true)
   var coverUnderlyingLayers by coverUnderlyingLayersProperty

   val zoomProperty = SimpleDoubleProperty(1.0)
   var zoom by zoomProperty
}