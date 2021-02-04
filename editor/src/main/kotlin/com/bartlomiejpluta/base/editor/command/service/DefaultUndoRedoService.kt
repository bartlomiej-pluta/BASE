package com.bartlomiejpluta.base.editor.command.service

import com.bartlomiejpluta.base.editor.command.model.Undoable
import org.springframework.stereotype.Service
import java.util.*

@Service
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
            undo.removeLast()
        }

        undo.push(undoable)
        redo.clear()
    }

    override fun undo() {
        if(undo.isNotEmpty()) {
            undo.pop().let {
                it.undo()
                redo.push(it)
            }
        }
    }

    override fun redo() {
        if(redo.isNotEmpty()) {
            redo.pop().let {
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
}