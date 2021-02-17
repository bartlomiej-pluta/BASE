package com.bartlomiejpluta.base.editor.map.canvas

import com.bartlomiejpluta.base.editor.map.model.enumeration.PassageAbility
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

object PassageAbilitySymbol {
   private const val SIZE = 0.7

   fun render(gc: GraphicsContext, x: Double, y: Double, w: Double, h: Double, passageAbility: PassageAbility) {
      val fill = gc.fill
      val alpha = gc.globalAlpha

      when (passageAbility) {
         PassageAbility.ALLOW -> allow(gc, x, y, w, h)
         PassageAbility.BLOCK -> block(gc, x, y, w, h)
         PassageAbility.UP_ONLY -> up(gc, x, y, w, h)
         PassageAbility.DOWN_ONLY -> down(gc, x, y, w, h)
         PassageAbility.LEFT_ONLY -> left(gc, x, y, w, h)
         PassageAbility.RIGHT_ONLY -> right(gc, x, y, w, h)
      }

      gc.fill = fill
      gc.globalAlpha = alpha
   }

   fun allow(gc: GraphicsContext, x: Double, y: Double, w: Double, h: Double) {
      gc.fill = Color.GREEN
      gc.globalAlpha = 0.1
      gc.fillRect(x, y, w, h)
   }

   fun block(gc: GraphicsContext, x: Double, y: Double, w: Double, h: Double) {
      gc.fill = Color.RED
      gc.globalAlpha = 0.4
      gc.fillRect(x, y, w, h)
   }

   fun down(gc: GraphicsContext, x: Double, y: Double, w: Double, h: Double) {
      gc.fill = Color.GREEN
      gc.globalAlpha = 0.1
      gc.fillRect(x, y, w, h)
      gc.globalAlpha = 1.0

      gc.fill = Color.WHITE
      gc.beginPath()
      gc.moveTo(x + (1 - SIZE) * w, y + (1 - SIZE) * h)
      gc.lineTo(x + w * SIZE, y + (1 - SIZE) * h)
      gc.lineTo(x + w / 2, y + h * SIZE)
      gc.closePath()

      gc.fill()
   }

   fun up(gc: GraphicsContext, x: Double, y: Double, w: Double, h: Double) {
      gc.fill = Color.GREEN
      gc.globalAlpha = 0.1
      gc.fillRect(x, y, w, h)
      gc.globalAlpha = 1.0

      gc.fill = Color.WHITE
      gc.beginPath()
      gc.moveTo(x + (1 - SIZE) * w, y + h * SIZE)
      gc.lineTo(x + w * SIZE, y + h * SIZE)
      gc.lineTo(x + w / 2, y + (1 - SIZE) * h)
      gc.closePath()

      gc.fill()
   }

   fun left(gc: GraphicsContext, x: Double, y: Double, w: Double, h: Double) {
      gc.fill = Color.GREEN
      gc.globalAlpha = 0.1
      gc.fillRect(x, y, w, h)
      gc.globalAlpha = 1.0

      gc.fill = Color.WHITE
      gc.beginPath()
      gc.moveTo(x + (1 - SIZE) * w, y + h / 2)
      gc.lineTo(x + SIZE * w, y + (1 - SIZE) * h)
      gc.lineTo(x + w * SIZE, y + h * SIZE)
      gc.closePath()

      gc.fill()
   }

   fun right(gc: GraphicsContext, x: Double, y: Double, w: Double, h: Double) {
      gc.fill = Color.GREEN
      gc.globalAlpha = 0.1
      gc.fillRect(x, y, w, h)
      gc.globalAlpha = 1.0

      gc.fill = Color.WHITE
      gc.beginPath()
      gc.moveTo(x + SIZE * w, y + h / 2)
      gc.lineTo(x + (1 - SIZE) * w, y + (1 - SIZE) * h)
      gc.lineTo(x + w * (1 - SIZE), y + h * SIZE)
      gc.closePath()

      gc.fill()
   }
}