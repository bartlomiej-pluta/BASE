package com.bartlomiejpluta.base.editor.view.fragment

import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.view.component.map.MapPane
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.transform.Scale
import tornadofx.Fragment
import tornadofx.group
import tornadofx.plusAssign
import tornadofx.scrollpane

class MapFragment : Fragment() {
    private val undoRedoService: UndoRedoService by di()
    private val pane = MapPane { undoRedoService.push(it) }
    val scaleProperty = SimpleDoubleProperty(1.0)

    private val transformation = Scale(1.0, 1.0, 0.0, 0.0).apply {
        xProperty().bind(scaleProperty)
        yProperty().bind(scaleProperty)
    }

    fun updateMap(map: GameMap) {
        pane.updateMap(map)
    }

    fun redraw() {
        pane.render()
    }

    override val root = scrollpane {
        prefWidth = 640.0
        prefHeight = 480.0

        group {
            group {
                this += pane
                transforms += transformation
            }
        }
    }
}
