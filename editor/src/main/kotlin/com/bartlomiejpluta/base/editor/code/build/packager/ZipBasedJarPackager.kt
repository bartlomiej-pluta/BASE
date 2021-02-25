package com.bartlomiejpluta.base.editor.code.build.packager

import org.springframework.stereotype.Component
import java.io.File
import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Component
class ZipBasedJarPackager : JarPackager {

   override fun pack(sourceDirectory: File, targetJar: File, root: String) {
      val env = mapOf("create" to "true")
      val zip = targetJar.toPath()
      val uri = URI.create("jar:" + zip.toUri())
      val source = sourceDirectory.toPath()

      FileSystems.newFileSystem(uri, env).use { jar ->
         Files.walk(source)
            .filter(Files::isRegularFile)
            .forEach {
               val path = jar.getPath(Paths.get(root, source.relativize(it).toString()).normalize().toString())
               Files.createDirectories(path.parent)
               Files.copy(it, path, StandardCopyOption.REPLACE_EXISTING)
            }
      }
   }
}