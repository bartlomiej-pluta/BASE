package com.bartlomiejpluta.base.editor.common.serial

import java.io.InputStream

interface Deserializer<T> {
   fun deserialize(input: InputStream): T
}