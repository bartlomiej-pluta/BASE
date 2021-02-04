package com.bartlomiejpluta.base.editor.command.service

import com.bartlomiejpluta.base.editor.command.model.Undoable
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class DefaultUndoRedoService : UndoRedoService {
    private val undo: Deque<Undoable> = ArrayDeque()
    private val redo: Deque<Undoable> = ArrayDeque()

    override var sizeMax = 30
        set(value) {
            if(value >= 0) {
                for(i in 0 until undo.size - value) {
                    undo.removeLast()
                }

                field = value
            }
        }

    override fun push(undoable: Undoable) {
        if(undo.size == sizeMax) {
            log.debug("The max size of [undo] list has been reached. Removing the last item...")
            undo.removeLast()
        }

        log.debug("Pushing item to [undo] list: ${undoable.commandName}")
        undo.push(undoable)
        redo.clear()
    }

    override fun undo() {
        if(undo.isNotEmpty()) {
            undo.pop().let {
                log.debug("Performing undo: ${it.commandName}")
                it.undo()
                redo.push(it)
            }
        }
    }

    override fun redo() {
        if(redo.isNotEmpty()) {
            redo.pop().let {
                log.debug("Performing redo: ${it.commandName}")
                it.redo()
                undo.push(it)
            }
        }
    }

    override val lastUndoable: Undoable?
        get() = undo.last

    override val lastRedoable: Undoable?
        get() = redo.last

    override val undoCommandName: String
        get() = undo.last?.commandName ?: ""

    override val redoCommandName: String
        get() = redo.last?.commandName ?: ""

    companion object {
        private val log = LogFactory.getLog(DefaultUndoRedoService::class.java)
    }
}