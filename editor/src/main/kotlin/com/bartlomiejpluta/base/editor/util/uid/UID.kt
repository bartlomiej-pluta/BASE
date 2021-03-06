package com.bartlomiejpluta.base.editor.util.uid

import java.util.*

object UID {
   fun next(sequence: Iterable<String>): String {
      var uid: String

      do {
         uid = UUID.randomUUID().toString()
      } while (uid in sequence)

      return uid
   }
}