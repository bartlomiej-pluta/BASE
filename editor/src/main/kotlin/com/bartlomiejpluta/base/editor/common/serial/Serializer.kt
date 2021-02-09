package com.bartlomiejpluta.base.editor.common.serial

import java.io.InputStream
import java.io.OutputStream

interface Serializer<T> {
   fun serialize(item: T, output: OutputStream)
}