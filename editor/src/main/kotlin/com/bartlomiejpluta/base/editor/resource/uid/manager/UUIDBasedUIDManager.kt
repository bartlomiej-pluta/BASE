package com.bartlomiejpluta.base.editor.resource.uid.manager

import com.bartlomiejpluta.base.editor.resource.uid.model.UIDTarget
import org.springframework.stereotype.Component
import java.util.*

@Component
class UUIDBasedUIDManager : UIDManager {
   private val registry = mutableMapOf<UIDTarget, Set<String>>()

   override fun nextUID(target: UIDTarget): String {
      val set = registry.putIfAbsent(target, mutableSetOf())!!

      var uid = ""

      do {
         uid = UUID.randomUUID().toString()
      } while (uid !in set)

      return uid
   }

   override fun loadData(target: UIDTarget, uids: Set<String>) {
      TODO("Not yet implemented")
   }
}