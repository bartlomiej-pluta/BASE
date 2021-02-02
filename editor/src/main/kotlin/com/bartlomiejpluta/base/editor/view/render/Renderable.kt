package com.bartlomiejpluta.base.editor.view.render

import javafx.scene.canvas.GraphicsContext

interface Renderable {
    fun render(gc: GraphicsContext)
}