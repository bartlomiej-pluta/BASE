package com.bartlomiejpluta.base.editor.command.model.base

interface Undoable {
   fun undo()
   fun redo()
   val commandName: String
}