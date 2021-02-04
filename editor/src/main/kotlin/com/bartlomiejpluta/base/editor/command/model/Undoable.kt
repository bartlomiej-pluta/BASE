package com.bartlomiejpluta.base.editor.command.model

interface Undoable {
    fun undo()
    fun redo()
    val commandName: String
}