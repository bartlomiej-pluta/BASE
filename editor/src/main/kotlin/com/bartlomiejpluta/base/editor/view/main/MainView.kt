package com.bartlomiejpluta.base.editor.view.main

import com.bartlomiejpluta.base.editor.controller.map.MapController
import com.bartlomiejpluta.base.editor.view.fragment.MapFragment
import tornadofx.*

class MainView : View() {
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
        }

        center = mapFragment.root
    }
}