package com.bartlomiejpluta.base.editor

import tornadofx.*


class MyView : View() {
    override val root = vbox {
        button("Press me")
        label("Waiting")
    }
}

class EditorApp : App(MyView::class)

fun main(args: Array<String>) {
    launch<EditorApp>(args)
}