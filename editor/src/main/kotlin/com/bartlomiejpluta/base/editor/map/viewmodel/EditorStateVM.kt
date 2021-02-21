package com.bartlomiejpluta.base.editor.map.viewmodel

import com.bartlomiejpluta.base.editor.map.model.layer.Layer
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.ViewModel
import tornadofx.getValue
import tornadofx.setValue

class EditorStateVM : ViewModel() {
   val selectedLayerIndexProperty = SimpleIntegerProperty(0)
   var selectedLayerIndex by selectedLayerIndexProperty

   val selectedLayerProperty = SimpleObjectProperty<Layer>()
   var selectedLayer by selectedLayerProperty

   val showGridProperty = SimpleBooleanProperty(true)
   var showGrid by showGridProperty

   val coverUnderlyingLayersProperty = SimpleBooleanProperty(true)
   var coverUnderlyingLayers by coverUnderlyingLayersProperty

   val zoomProperty = SimpleDoubleProperty(1.0)
   var zoom by zoomProperty

   val cursorRowProperty = SimpleIntegerProperty(-1)
   val cursorRow by cursorRowProperty

   val cursorColumnProperty = SimpleIntegerProperty(-1)
   val cursorColumn by cursorColumnProperty

   val takingSnapshotProperty = SimpleBooleanProperty(false)
   var takingSnapshot by takingSnapshotProperty
}