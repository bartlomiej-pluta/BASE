package com.bartlomiejpluta.base.editor

import com.bartlomiejpluta.base.editor.view.main.MainView
import tornadofx.*


class EditorApp : App(MainView::class)

fun main(args: Array<String>) {
    launch<EditorApp>(args)
}