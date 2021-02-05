package com.bartlomiejpluta.base.editor.view.main

import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.controller.map.MapController
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.view.map.MapFragment
import javafx.scene.control.TabPane
import tornadofx.*


class MainView : View() {
   private val undoRedoService: UndoRedoService by di()
   private val mapController: MapController by di()
   private val tabPane = TabPane()

   override val root = borderpane {
      top = hbox {
         button("Map 1") {
            action {
               val map = mapController.getMap(1)
               tabPane += find<MapFragment>(Scope(), MapFragment::map to map).apply {
                  title = "Map 1"
               }
            }
         }

         button("Map 2") {
            action {
               val map = mapController.getMap(2)
               tabPane += find<MapFragment>(Scope(), MapFragment::map to map).apply {
                  title = "Map 2"
               }
            }
         }

         button("Undo") {
            action {
               undoRedoService.undo()
               fire(RedrawMapRequestEvent)
            }
         }

         button("Redo") {
            action {
               undoRedoService.redo()
               fire(RedrawMapRequestEvent)
            }
         }
      }

      center = tabPane
   }
}