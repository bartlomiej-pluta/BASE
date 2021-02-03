package com.bartlomiejpluta.base.editor.view.fragment

import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.view.component.map.MapPane
import tornadofx.Fragment
import tornadofx.group
import tornadofx.plusAssign
import tornadofx.scrollpane

class MapFragment : Fragment() {
    val map: GameMap by param()
    val pane = MapPane(map)

    override val root = scrollpane {
        prefWidth = 300.0
        prefHeight = 300.0

        group {
            group {
                this += pane
            }
        }
    }
}
