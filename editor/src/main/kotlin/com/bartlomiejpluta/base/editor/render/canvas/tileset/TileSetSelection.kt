package com.bartlomiejpluta.base.editor.render.canvas.tileset

import com.bartlomiejpluta.base.editor.model.tileset.TileSet
import com.bartlomiejpluta.base.editor.render.model.Renderable
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import org.apache.commons.logging.LogFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.abs
import kotlin.math.min


class TileSetSelection(tileSet: TileSet) : Renderable {
    private val tileWidth = tileSet.tileWidth.toDouble()
    private val tileHeight = tileSet.tileHeight.toDouble()

    private var startRow = 0.0
    private var startColumn = 0.0
    private var offsetRow = 0.0
    private var offsetColumn = 0.0
    private var x = 0.0
    private var y = 0.0
    private var width = tileWidth
    private var height = tileHeight

    fun begin(row: Double, column: Double) {
        startRow = row
        offsetRow = 0.0
        startColumn = column
        offsetColumn = 0.0

        updateRect(row, column)
    }

    private fun updateRect(row: Double, column: Double) {
        x = min(column, startColumn) * tileWidth
        y = min(row, startRow) * tileHeight
        width = (offsetColumn + 1) * tileWidth
        height = (offsetRow + 1) * tileHeight
    }

    fun finish(row: Double, column: Double) {
        offsetRow = abs(row - startRow)
        offsetColumn = abs(column - startColumn)

        updateRect(row, column)
    }

    override fun render(gc: GraphicsContext) {
        gc.fill = SELECTION_FILL_COLOR
        gc.fillRect(x, y, width, height)

        gc.stroke = SELECTION_STROKE_COLOR
        gc.strokeRect(x, y, width, height)
    }

    companion object {
        private val SELECTION_FILL_COLOR = Color.color(0.0, 0.7, 1.0, 0.4)
        private val SELECTION_STROKE_COLOR = Color.color(0.0, 0.7, 1.0, 1.0)
    }
}