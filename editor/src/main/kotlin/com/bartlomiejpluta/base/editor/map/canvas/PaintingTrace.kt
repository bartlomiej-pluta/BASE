package com.bartlomiejpluta.base.editor.map.canvas

import com.bartlomiejpluta.base.editor.command.model.base.Undoable
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM

interface PaintingTrace : Undoable {
   fun beginTrace(editorStateVM: EditorStateVM, brushVM: BrushVM)
   fun proceedTrace(editorStateVM: EditorStateVM, brushVM: BrushVM)
   fun commitTrace(editorStateVM: EditorStateVM, brushVM: BrushVM)
}