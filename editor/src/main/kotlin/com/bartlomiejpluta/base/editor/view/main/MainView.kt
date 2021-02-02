package com.bartlomiejpluta.base.editor.view.main

import com.bartlomiejpluta.base.editor.controller.map.MapController
import com.bartlomiejpluta.base.editor.view.fragment.MapFragment
import tornadofx.View
import tornadofx.borderpane

class MainView : View() {
    private val mapController: MapController by inject()

    override val root = borderpane {
        center = find<MapFragment>(mapOf(MapFragment::map to mapController.map)).root
    }
}