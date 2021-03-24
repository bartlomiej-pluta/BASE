package com.bartlomiejpluta.base.editor.database.service

import com.bartlomiejpluta.base.editor.database.model.Table

interface DatabaseService {
   val tables: List<Table>
}