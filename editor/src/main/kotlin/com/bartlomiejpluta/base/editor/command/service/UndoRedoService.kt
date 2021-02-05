package com.bartlomiejpluta.base.editor.command.service

import com.bartlomiejpluta.base.editor.command.model.Undoable

interface UndoRedoService {
   fun push(undoable: Undoable)
   fun undo()
   fun redo()

   val lastUndoable: Undoable?
   val lastRedoable: Undoable?
   val undoCommandName: String
   val redoCommandName: String
   var sizeMax: Int
}