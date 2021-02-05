package com.bartlomiejpluta.base.editor.view.fragment

import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.view.component.map.MapPane
import com.bartlomiejpluta.base.editor.view.component.tileset.TileSetPane
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import com.bartlomiejpluta.base.editor.viewmodel.map.BrushVM
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.TableView
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.transform.Scale
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*


class MapFragment : Fragment() {
    private val undoRedoService: UndoRedoService by di()

    val map: GameMap by param()

    private val brushVM = find<BrushVM>().apply { item = map.tileSet.baseBrush }
    private val mapVM = find<GameMapVM>().apply { item = map }

    val scaleProperty = SimpleDoubleProperty(1.0)

    private val selectedLayer = SimpleIntegerProperty(0)

    private val mapPane = MapPane(mapVM, brushVM, selectedLayer) { undoRedoService.push(it) }
    private val tileSetPane = TileSetPane(map.tileSet, brushVM)
    private var layersPane: TableView<Layer> by singleAssign()

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
                    layersPane = tableview(mapVM.layers) {
                        column("Layer Name", Layer::nameProperty).makeEditable()

                        selectedLayer.bind(selectionModel.selectedIndexProperty())
                    }

                    center = layersPane

                    bottom = toolbar {
                        button(graphic = FontIcon("fa-plus")) {
                            action {
                                mapVM.item.createTileLayer("Layer ${mapVM.layers.size+1}")
                                layersPane.selectionModel.select(mapVM.layers.size - 1)
                            }
                        }

                        button(graphic = FontIcon("fa-chevron-up")) {
                            enableWhen(selectedLayer.greaterThan(0))
                            action {
                                val newIndex = selectedLayer.value-1
                                mapVM.layers.swap(selectedLayer.value, newIndex)
                                layersPane.selectionModel.select(newIndex)
                                fire(RedrawMapRequestEvent)
                            }
                        }

                        button(graphic = FontIcon("fa-chevron-down")) {
                            enableWhen(selectedLayer.lessThan(mapVM.layers.sizeProperty().minus(1)).and(selectedLayer.greaterThanOrEqualTo(0)))
                            action {
                                val newIndex = selectedLayer.value+1
                                mapVM.layers.swap(selectedLayer.value, newIndex)
                                layersPane.selectionModel.select(newIndex)
                                fire(RedrawMapRequestEvent)
                            }
                        }

                        button(graphic = FontIcon("fa-trash")) {
                            enableWhen(selectedLayer.greaterThanOrEqualTo(0))
                            action {
                                var index = selectedLayer.value
                                mapVM.layers.removeAt(index)

                                if(--index >= 0) {
                                    layersPane.selectionModel.select(index)
                                }

                                fire(RedrawMapRequestEvent)
                            }
                        }
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
