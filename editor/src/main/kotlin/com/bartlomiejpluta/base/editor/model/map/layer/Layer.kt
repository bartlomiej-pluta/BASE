package com.bartlomiejpluta.base.editor.model.map.layer

import javafx.beans.property.StringProperty

interface Layer {
    val name: String
    val nameProperty: StringProperty
}