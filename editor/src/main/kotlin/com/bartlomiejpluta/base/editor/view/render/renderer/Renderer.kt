package com.bartlomiejpluta.base.editor.view.render.renderer

import com.bartlomiejpluta.base.editor.render.model.Renderable
import javafx.animation.AnimationTimer
import javafx.scene.canvas.GraphicsContext

class Renderer(
    private val gc: GraphicsContext,
    private val renderable: Renderable
) : AnimationTimer() {
    private var previous = System.nanoTime()

    override fun handle(now: Long) {
        val dt = (now - previous) / 1000000000.0
        previous = now
        gc.isImageSmoothing = false

        render()
    }

    private fun render() {
        gc.clearRect(0.0, 0.0, gc.canvas.width, gc.canvas.height);

        renderable.render(gc)
    }
}