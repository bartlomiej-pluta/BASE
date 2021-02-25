package com.bartlomiejpluta.base.editor.code.build.packager

import java.io.File

interface JarPackager {
   fun pack(sourceDirectory: File, targetJar: File, root: String = ".")
}