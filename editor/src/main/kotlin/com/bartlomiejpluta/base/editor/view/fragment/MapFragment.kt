package com.bartlomiejpluta.base.editor.view.fragment

import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.view.component.map.MapPane
import com.bartlomiejpluta.base.editor.view.component.tileset.TileSetPane
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.transform.Scale
import tornadofx.*


class MapFragment : Fragment() {
    private val undoRedoService: UndoRedoService by di()
    val scaleProperty = SimpleDoubleProperty(1.0)
    val map: GameMap by param()

    private val mapPane = MapPane(map) { undoRedoService.push(it) }
    private val tileSetPane = TileSetPane(map.tileSet) { mapPane.setBrush(it) }

    private val transformation = Scale(1.0, 1.0, 0.0, 0.0).apply {
        xProperty().bind(scaleProperty)
        yProperty().bind(scaleProperty)
    }

    init {
        subscribe<RedrawMapRequestEvent> { mapPane.render() }
    }

    override val root = borderpane {
        center = scrollpane {
            prefWidth = 640.0
            prefHeight = 480.0
            isPannable = true

            group {

                // Let the ScrollPane.viewRect only pan on middle button.
                addEventHandler(MouseEvent.ANY) {
                    if (it.button != MouseButton.MIDDLE) {
                        it.consume()
                    }
                }

                group {
                    this += mapPane
                    transforms += transformation
                }
            }
        }

        right = drawer(multiselect = true) {
            item("Layers", expanded = true) {
                borderpane {
                    center = listview(observableListOf("Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6"))
                    bottom = hbox {
                        button("New")
                        button("Up")
                        button("Down")
                        button("Delete")
                    }
                }
            }

            item("Tile Set", expanded = true) {
                scrollpane {
                    maxHeightProperty().bind(this@item.heightProperty())
                    this += tileSetPane
                }
            }
        }
    }
}
