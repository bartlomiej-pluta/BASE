package com.bartlomiejpluta.base.editor.database.model.schema

import javafx.scene.Node
import java.sql.Connection

interface Schema {
   val name: String

   fun rename(connection: Connection, newName: String)

   fun delete(connection: Connection)

   val icon: Node?
}