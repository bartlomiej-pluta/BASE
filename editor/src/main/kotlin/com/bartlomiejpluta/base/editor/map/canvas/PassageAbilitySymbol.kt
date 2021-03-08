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
}