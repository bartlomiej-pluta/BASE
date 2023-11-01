package com.bartlomiejpluta.base.editor.code.build.generator

import com.bartlomiejpluta.base.api.context.Context
import com.bartlomiejpluta.base.api.map.handler.MapHandler
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.map.model.layer.ObjectLayer
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.project.model.Project
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
            .forEach { generateMapObjects(it.first, it.second, project) }
      }
   }

   private fun generateMapObjects(asset: GameMapAsset, map: GameMap, project: Project) {
      val runner = className(project.runner)

      map.layers.forEachIndexed { index, layer ->
         if (layer is ObjectLayer) {
            generateLayerClass(project.buildGeneratedCodeDirectory, asset, map, layer, index, runner)
         }
      }
   }

   private fun generateLayerClass(
      directory: File,
      asset: GameMapAsset,
      map: GameMap,
      layer: ObjectLayer,
      layerIndex: Int,
      runner: ClassName
   ) {
      val packageName = "com.bartlomiejpluta.base.generated.map"
      val className = ClassName.get(
         packageName,
         format("MapInitializer_%s\$\$Layer%d", map.uid.replace("-", "_"), map.layers.indexOf(layer))
      )

      val annotation = AnnotationSpec.builder(Generated::class.java)
         .addMember("value", "\$S", GENERATOR_NAME)
         .addMember("date", "\$S", DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
         .addMember("comments", "\$S", "Initializer for map '${asset.name}' (UID: ${asset.uid})")
         .build()

      val handler = className(map.handler)
      val generatedClass = TypeSpec.classBuilder(className)
         .addSuperinterface(MapInitializer::class.java)
         .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
         .addAnnotation(annotation)

      layer.objects.forEach {
         MethodSpec.methodBuilder("_${it.x}x${it.y}")
            .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            .addParameter(Context::class.java, "context", Modifier.FINAL)
            .addParameter(runner, "runner", Modifier.FINAL)
            .addParameter(handler, "handler", Modifier.FINAL)
            .addParameter(EngineGameMap::class.java, "map", Modifier.FINAL)
            .addParameter(EngineObjectLayer::class.java, "layer", Modifier.FINAL)
            .addParameter(TypeName.INT, "x", Modifier.FINAL)
            .addParameter(TypeName.INT, "y", Modifier.FINAL)
            .addParameter(MAP_PIN_TYPE, "here", Modifier.FINAL)
            .addCode(it.code)
            .build()
            .let(generatedClass::addMethod)
      }

      val runMethod = MethodSpec.methodBuilder("run")
         .addAnnotation(Override::class.java)
         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
         .addParameter(Context::class.java, "context", Modifier.FINAL)
         .addParameter(MapHandler::class.java, "handler", Modifier.FINAL)
         .addParameter(EngineGameMap::class.java, "map", Modifier.FINAL)
         .addParameter(EngineObjectLayer::class.java, "layer", Modifier.FINAL)
         .addStatement("var customRunner = (\$T) context.getGameRunner()", runner)
         .addStatement("var customHandler = (\$T) handler", handler)

      layer.objects.forEach {
         runMethod.addStatement("_${it.x}x${it.y}(context, customRunner, customHandler, map, layer, ${it.x}, ${it.y}, \$T.builder().map(\"${map.uid}\").layer($layerIndex).x(${it.x}).y(${it.y}).build())", MAP_PIN_TYPE)
      }

      generatedClass
         .addMethod(runMethod.build())

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
               for (i in layer.javaImports.lines()) {
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