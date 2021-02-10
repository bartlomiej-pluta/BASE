package com.bartlomiejpluta.base.editor.resource.uid.manager

import com.bartlomiejpluta.base.editor.resource.uid.model.UIDTarget

interface UIDManager {
   fun nextUID(target: UIDTarget): String
   fun loadData(target: UIDTarget, uids: Set<String>)
}