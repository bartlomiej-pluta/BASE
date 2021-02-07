package com.bartlomiejpluta.base.editor.command.service

import com.bartlomiejpluta.base.editor.command.context.UndoableContext
import com.bartlomiejpluta.base.editor.command.model.base.Undoable
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.lang.Integer.toHexString
import java.util.*

@Component
class DefaultUndoRedoService : UndoRedoService {
   private val undo: Deque<Pair<Undoable, UndoableContext>> = ArrayDeque()
   private val redo: Deque<Pair<Undoable, UndoableContext>> = ArrayDeque()

   var sizeMax = 30
      set(value) {
         if (value >= 0) {
            for (i in 0 until undo.size - value) {
               undo.removeLast()
            }

            field = value
         }
      }

   override fun push(undoable: Undoable) {
      push(undoable, GLOBAL_CONTEXT)
   }

   override fun push(undoable: Undoable, context: UndoableContext) {
      if (undo.size == sizeMax) {
         log.debug("The max size of [undo] stack has been reached. Removing the last item...")
         undo.removeLast()
      }

      log.debug("Pushing item to [undo] stack: ${undoable.commandName} (ctx: ${toHexString(context.hashCode())})")
      undo.push(undoable to context)
      redo.clear()
   }

   override fun undo() {
      if (undo.isNotEmpty()) {
         undo.pop().let {
            log.debug("Performing undo: ${it.first.commandName}")
            it.first.undo()
            redo.push(it)
         }
      }
   }

   override fun undo(context: UndoableContext) {
      if (undo.isNotEmpty()) {
         undo.firstOrNull { it.second === context }?.let {
            log.debug("Performing contextual (ctx: ${toHexString(context.hashCode())}) undo: ${it.first.commandName}")
            undo.remove(it)
            it.first.undo()
            redo.push(it)
         }
      }
   }

   override fun redo() {
      if (redo.isNotEmpty()) {
         redo.pop().let {
            log.debug("Performing redo: ${it.first.commandName}")
            it.first.redo()
            undo.push(it)
         }
      }
   }

   override fun redo(context: UndoableContext) {
      if (redo.isNotEmpty()) {
         redo.firstOrNull { it.second === context }?.let {
            log.debug("Performing contextual (ctx: ${toHexString(context.hashCode())}) redo: ${it.first.commandName}")
            redo.remove(it)
            it.first.redo()
            undo.push(it)
         }
      }
   }

   override fun clear() {
      log.debug("Clearing [undo] and [redo] stacks")
      undo.clear()
      redo.clear()
   }

   override fun clear(context: UndoableContext) {
      log.debug("Clearing [undo] and [redo] stacks (ctx: ${toHexString(context.hashCode())})")
      undo.removeIf { it.second == context }
      redo.removeIf { it.second == context }
   }

   override val lastUndoable: Undoable?
      get() = undo.first?.first

   override val lastRedoable: Undoable?
      get() = redo.first?.first

   override val undoCommandName: String
      get() = undo.first?.first?.commandName ?: ""

   override val redoCommandName: String
      get() = redo.first?.first?.commandName ?: ""

   override fun lastUndoable(context: UndoableContext) = undo.firstOrNull { it.second === context }?.first

   override fun lastRedoable(context: UndoableContext) = redo.firstOrNull { it.second === context }?.first

   override fun undoCommandName(context: UndoableContext) =
      undo.firstOrNull { it.second === context }?.first?.commandName ?: ""

   override fun redoCommandName(context: UndoableContext) =
      redo.firstOrNull { it.second === context }?.first?.commandName ?: ""


   companion object {
      private val log = LoggerFactory.getLogger(DefaultUndoRedoService::class.java)
      private val GLOBAL_CONTEXT = object : UndoableContext {}
   }
}