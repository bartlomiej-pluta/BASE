package com.bartlomiejpluta.base.editor.view.main

import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.controller.map.MapController
import com.bartlomiejpluta.base.editor.view.fragment.MapFragment
import tornadofx.*

class MainView : View() {
    private val undoRedoService: UndoRedoService by di()
    private val mapController: MapController by di()
    private val mapFragment = find<MapFragment>()

    override val root = borderpane {
        top = hbox {
            button("Map 1") {
                action {
                    mapFragment.updateMap(mapController.getMap(1))
                }
            }

            button("Map 2") {
                action {
                    mapFragment.updateMap(mapController.getMap(2))
                }
            }

            button("+") {
                action {
                    mapFragment.scaleProperty.value += 0.1
                }
            }

            button("-") {
                action {
                    mapFragment.scaleProperty.value -= 0.1
                }
            }

            button("Undo") {
                action {
                    undoRedoService.undo()
                    mapFragment.redraw()
                }
            }

            button("Redo") {
                action {
                    undoRedoService.redo()
                    mapFragment.redraw()
                }
            }
        }

        center = mapFragment.root
    }
}