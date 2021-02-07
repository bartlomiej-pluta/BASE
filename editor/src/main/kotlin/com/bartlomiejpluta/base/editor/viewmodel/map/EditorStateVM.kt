package com.bartlomiejpluta.base.editor.viewmodel.map

import com.bartlomiejpluta.base.editor.model.map.editor.EditorState
import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import tornadofx.*

class EditorStateVM : ItemViewModel<EditorState>(EditorState()) {
   val selectedLayerProperty = bind(EditorState::selectedLayer) as IntegerProperty
   var selectedLayer by selectedLayerProperty

   val showGridProperty = bind(EditorState::showGrid)
   var showGrid by showGridProperty

   val coverUnderlyingLayersProperty = bind(EditorState::coverUnderlyingLayers)
   var coverUnderlyingLayers by coverUnderlyingLayersProperty

   val zoomProperty = bind(EditorState::zoom) as DoubleProperty
   var zoom by zoomProperty
}