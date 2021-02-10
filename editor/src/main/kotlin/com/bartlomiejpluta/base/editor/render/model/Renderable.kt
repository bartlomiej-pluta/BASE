package com.bartlomiejpluta.base.editor.render.model

import javafx.scene.canvas.GraphicsContext

interface Renderable {
   fun render(gc: GraphicsContext)
}