package com.bartlomiejpluta.base.editor.command.service

import com.bartlomiejpluta.base.editor.command.context.UndoableContext
import com.bartlomiejpluta.base.editor.command.model.base.Undoable

interface UndoRedoService {
   fun push(undoable: Undoable)
   fun undo()
   fun redo()
   fun clear()

   fun push(undoable: Undoable, context: UndoableContext)
   fun undo(context: UndoableContext)
   fun redo(context: UndoableContext)
   fun clear(context: UndoableContext)

   val lastUndoable: Undoable?
   val lastRedoable: Undoable?
   val undoCommandName: String
   val redoCommandName: String

   fun lastUndoable(context: UndoableContext): Undoable?
   fun lastRedoable(context: UndoableContext): Undoable?
   fun undoCommandName(context: UndoableContext): String
   fun redoCommandName(context: UndoableContext): String
}