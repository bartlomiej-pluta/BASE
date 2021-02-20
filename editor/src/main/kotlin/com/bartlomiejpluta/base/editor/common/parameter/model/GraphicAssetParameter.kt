package com.bartlomiejpluta.base.editor.common.parameter.model

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.asset.view.select.SelectGraphicAssetFragment
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import tornadofx.Scope
import tornadofx.find
import tornadofx.select

class GraphicAssetParameter<T : Asset>(
   key: String,
   initialValue: T,
   editable: Boolean = true,
   assets: ObservableList<T>,
   onCommit: (oldValue: T, newValue: T, submit: () -> Unit) -> Unit = { _, _, submit -> submit() }
) : Parameter<T>(key, initialValue, editable, false, onCommit) {
   override val editorValueProperty = SimpleObjectProperty(initialValue)

   override val editor = Label(initialValue.name).apply {
      textProperty().bind(editorValueProperty.select { it.nameProperty })

      addEventHandler(MouseEvent.MOUSE_CLICKED) {
         if (it.button == MouseButton.PRIMARY) {
            find<SelectGraphicAssetFragment<T>>(Scope(), SelectGraphicAssetFragment<T>::assets to assets).apply {
               onComplete { asset ->
                  editorValueProperty.value = asset
                  commit()
               }

               openModal(block = true, resizable = false)
            }
            it.consume()
         }
      }
   }

   override val valueString: String
      get() = value.name

   init {
      super.init()
   }
}