package com.bartlomiejpluta.base.editor.command.model.base

class SimpleCommand<T>(
   override val commandName: String,
   private val formerValue: T,
   private val value: T,
   private val execute: (T) -> Unit
) : Undoable, Command {

   override fun undo() {
      execute(formerValue)
   }

   override fun redo() {
      execute()
   }

   override fun execute() {
      execute(value)
   }
}