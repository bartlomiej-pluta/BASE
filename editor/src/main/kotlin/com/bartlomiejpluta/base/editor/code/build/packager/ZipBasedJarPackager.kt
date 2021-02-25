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
            .forEach { file ->
               val path = jar.getPath(Paths.get(root, source.relativize(file).toString()).normalize().toString())
               path.parent?.let { Files.createDirectories(it) }
               Files.copy(file, path, StandardCopyOption.REPLACE_EXISTING)
            }
      }
   }

   override fun copy(file: File, targetJar: File, root: String) {
      val env = mapOf("create" to "true")
      val zip = targetJar.toPath()
      val uri = URI.create("jar:" + zip.toUri())

      FileSystems.newFileSystem(uri, env).use { jar ->
         val path = jar.getPath(Paths.get(root, file.name).normalize().toString())
         file.inputStream().use { fis -> Files.copy(fis, path) }
      }
   }
}