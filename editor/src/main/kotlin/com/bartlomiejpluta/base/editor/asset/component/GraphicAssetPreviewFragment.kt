package com.bartlomiejpluta.base.editor.asset.component

import com.bartlomiejpluta.base.editor.main.component.EditorFragment
import javafx.scene.input.KeyEvent

class GraphicAssetPreviewFragment : EditorFragment() {
   private val preview = find<GraphicAssetPreviewView>()

   override val root = preview.root

   override fun handleShortcut(event: KeyEvent) {

   }
}