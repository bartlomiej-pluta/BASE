package com.bartlomiejpluta.base.editor.view.fragment

import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.view.component.map.MapPane
import tornadofx.Fragment
import tornadofx.group
import tornadofx.plusAssign
import tornadofx.scrollpane

class MapFragment : Fragment() {
    private val pane = MapPane()

    fun updateMap(map: GameMap) {
        pane.updateMap(map)
    }

    override val root = scrollpane {
        prefWidth = 640.0
        prefHeight = 480.0

        group {
            group {
                this += pane
            }
        }
    }
}
