package com.bartlomiejpluta.base.editor.viewmodel.map

import com.bartlomiejpluta.base.editor.model.map.editor.EditorOptions
import javafx.beans.property.IntegerProperty
import tornadofx.*

class EditorOptionsVM : ItemViewModel<EditorOptions>(EditorOptions()) {
   val selectedLayerProperty = bind(EditorOptions::selectedLayer) as IntegerProperty
   var selectedLayer by selectedLayerProperty

   val showGridProperty = bind(EditorOptions::showGrid)
   var showGrid by showGridProperty

   val coverUnderlyingLayersProperty = bind(EditorOptions::coverUnderlyingLayers)
   var coverUnderlyingLayers by coverUnderlyingLayersProperty
}