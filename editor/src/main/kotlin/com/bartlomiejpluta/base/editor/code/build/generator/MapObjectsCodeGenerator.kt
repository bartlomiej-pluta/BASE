package com.bartlomiejpluta.base.editor.code.build.generator

import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.map.model.layer.ObjectLayer
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.internal.map.MapInitializer
import com.squareup.javapoet.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.lang.String.format
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.annotation.processing.Generated
import javax.lang.model.element.Modifier
import com.bartlomiejpluta.base.api.map.layer.`object`.ObjectLayer as EngineObjectLayer
import com.bartlomiejpluta.base.api.map.model.GameMap as EngineGameMap

@Component
class MapObjectsCodeGenerator : CodeGenerator {

   @Autowired
   private lateinit var projectContext: ProjectContext

   override fun generate() {
      projectContext.project?.let { project ->
         project.maps
            .map { it to projectContext.loadMap(it.uid) }
            .forEach { generateMapInitializer(project.buildGeneratedCodeDirectory, it.first, it.second) }
      }
   }

   private fun generateMapInitializer(directory: File, asset: GameMapAsset, map: GameMap) {
      val packageName = "com.bartlomiejpluta.base.generated.map"
      val className = ClassName.get(
         packageName,
         format("MapInitializer_%s", map.uid.replace("-", "_"))
      )

      val annotation = AnnotationSpec.builder(Generated::class.java)
         .addMember("value", "\$S", GENERATOR_NAME)
         .addMember("date", "\$S", DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
         .addMember("comments", "\$S", "Initializer for map '${asset.name}' (UID: ${asset.uid})")
         .build()

      val generatedClass = TypeSpec.classBuilder(className)
         .addSuperinterface(MapInitializer::class.java)
         .superclass(className(map.handler))
         .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
         .addAnnotation(annotation)

      map.layers.forEachIndexed { index, layer ->
         if (layer is ObjectLayer) {
            layer.objects.forEach {
               MethodSpec.methodBuilder("_layer${index}_${it.x}x${it.y}")
                  .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                  .addParameter(EngineGameMap::class.java, "map", Modifier.FINAL)
                  .addParameter(EngineObjectLayer::class.java, "layer", Modifier.FINAL)
                  .addParameter(TypeName.INT, "x", Modifier.FINAL)
                  .addParameter(TypeName.INT, "y", Modifier.FINAL)
                  .addParameter(MAP_PIN_TYPE, "here", Modifier.FINAL)
                  .addCode(it.code.replace("\$", "\$\$"))
                  .build()
                  .let(generatedClass::addMethod)
            }
         }
      }

      val initializeMethod = MethodSpec.methodBuilder("initialize")
         .addAnnotation(Override::class.java)
         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)

      map.layers.forEachIndexed { index, layer ->
         if (layer is ObjectLayer) {
            layer.objects.forEach {
               initializeMethod.addStatement("_layer${index}_${it.x}x${it.y}(map, map.getObjectLayer(${index}), ${it.x}, ${it.y}, \$T.builder().map(\"${map.uid}\").layer($index).x(${it.x}).y(${it.y}).build())", MAP_PIN_TYPE)
            }
         }
      }

      generatedClass
         .addMethod(initializeMethod.build())

      val javaFile = JavaFile
         .builder(packageName, generatedClass.build())
         .build()

      val rawSource = javaFile.toString()
      val path = javaFile.toJavaFileObject().toUri().path
      val file = File(directory, path)
      file.parentFile.mkdirs()
      file.printWriter().apply {
         for (s in rawSource.lines()) {
            println(s)
            if (s.startsWith("package ")) {
               println()
               println("// User imports")
               for (i in map.javaImports.lines()) {
                  println(i)
               }
               println("// End of user imports")
               println()
            }
         }

         flush()
      }
   }

   private fun className(canonical: String) = ClassName.get(
      canonical.substringBeforeLast("."),
      canonical.substringAfterLast(".")
   )

   companion object {
      private val GENERATOR_NAME = MapObjectsCodeGenerator::class.java.canonicalName
      val MAP_PIN_TYPE = ClassName.get("com.bartlomiejpluta.base.api.map.layer.object", "MapPin")
   }
}