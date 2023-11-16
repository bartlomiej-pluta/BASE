package com.bartlomiejpluta.base.editor.code.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.nio.file.Path

@Component
class PrologFileService {

   @Autowired
   private lateinit var appContext: ApplicationContext

   fun toPathString(className: String) = toPath(className).toString() + ".pl"
   fun toPath(className: String) = Path.of("", *className.split(".").toTypedArray())

   fun ofPath(path: String): String = ofPath(Path.of(path))
   fun ofPath(path: Path): String = path.joinToString(".")
}