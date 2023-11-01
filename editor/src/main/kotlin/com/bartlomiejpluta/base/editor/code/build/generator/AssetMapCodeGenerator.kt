package com.bartlomiejpluta.base.editor.code.build.generator

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.map.model.layer.ObjectLayer
import com.bartlomiejpluta.base.editor.map.serial.MapDeserializer
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.project.model.Project
import com.squareup.javapoet.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import javax.annotation.processing.Generated
import javax.lang.model.element.Modifier

@Component
class AssetMapCodeGenerator : CodeGenerator {

   @Autowired
   private lateinit var projectContext: ProjectContext

   @Autowired
   private lateinit var mapDeserializer: MapDeserializer

   override fun generate() {
      projectContext.project?.let(::generateAssetClasses)
   }

   private fun generateAssetClasses(project: Project) {
      listOf(
         generateMapAssetClass("maps", project.maps),
         generateAssetClass("tilesets", project.tileSets),
         generateAssetClass("autotiles", project.autoTiles),
         generateAssetClass("charsets", project.characterSets),
         generateAssetClass("images", project.images),
         generateAssetClass("animations", project.animations),
         generateAssetClass("iconsets", project.iconSets),
         generateAssetClass("fonts", project.fonts),
         generateAssetClass("widgets", project.widgets),
         generateAssetClass("sounds", project.sounds)
      ).forEach {
         JavaFile
            .builder("A", it)
            .build()
            .writeTo(project.buildGeneratedCodeDirectory)
      }

   }

   private fun generateAssetClass(name: String, assets: List<Asset>): TypeSpec {
      val generatedAnnotation = AnnotationSpec.builder(Generated::class.java).addMember("value", "\$S", GENERATOR_NAME)
         .addMember("date", "\$S", DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
         .addMember("comments", "\$S", "Utility class for $name assets")
         .build()

      val className = ClassName.get("A", name)

      return TypeSpec
         .classBuilder(className)
         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
         .addAnnotation(generatedAnnotation)
         .addField(
            FieldSpec.builder(
               ParameterizedTypeName.get(
                  ClassName.get(java.util.Map::class.java),
                  ClassName.get(String::class.java),
                  className
               ), "_map", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL
            )
               .initializer("new \$T<>()", java.util.HashMap::class.java)
               .build()
         )
         .addStaticBlock(CodeBlock.builder()
            .apply {
               assets.forEach {
                  addStatement("_map.put(\"${it.name}\", ${getAssetName(it)})")
               }
            }
            .build()
         )
         .addField(String::class.java, "$", Modifier.PUBLIC, Modifier.FINAL)
         .addMethod(
            MethodSpec
               .constructorBuilder()
               .addModifiers(Modifier.PRIVATE)
               .addParameter(TypeName.get(String::class.java), "uid")
               .addStatement("this.\$\$ = uid")
               .build()
         )
         .addMethod(
            MethodSpec
               .methodBuilder("get")
               .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
               .returns(className)
               .addParameter(TypeName.get(String::class.java), "name")
               .addStatement("return _map.get(name)")
               .build()
         )
         .apply {
            assets.forEach {
               addField(
                  FieldSpec
                     .builder(className, getAssetName(it), Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                     .initializer("new ${className}(\"\$L\")", it.uid)
                     .build()
               )
            }
         }
         .build()
   }

   private fun generateMapAssetClass(name: String, assets: List<GameMapAsset>): TypeSpec {
      val generatedAnnotation = AnnotationSpec.builder(Generated::class.java).addMember("value", "\$S", GENERATOR_NAME)
         .addMember("date", "\$S", DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
         .addMember("comments", "\$S", "Utility class for $name assets")
         .build()

      val className = ClassName.get("A", name)
      val mapLayers = assets
         .map { asset -> asset to mapDeserializer.deserialize(asset.file.inputStream()) }
         .associate { (asset, map) -> asset to map.layers }

      val abstractAssetClassName = ClassName.get("", "GameMapAsset")
      val abstractLayerClassName = ClassName.get("", "GameMapAssetLayer")

      return TypeSpec
         .classBuilder(className)
         .addAnnotation(generatedAnnotation)
         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
         .addField(
            FieldSpec.builder(
               ParameterizedTypeName.get(
                  ClassName.get(java.util.Map::class.java),
                  ClassName.get(String::class.java),
                  abstractAssetClassName
               ), "_maps", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL
            )
               .initializer("new \$T<>()", java.util.HashMap::class.java)
               .build()
         )
         .addStaticBlock(CodeBlock.builder()
            .apply {
               assets.forEach {
                  addStatement("_maps.put(\"${it.name}\", ${getAssetName(it)})")
               }
            }
            .build()
         )
         .addMethod(
            MethodSpec
               .methodBuilder("byName")
               .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
               .returns(abstractAssetClassName)
               .addParameter(TypeName.get(String::class.java), "name")
               .addStatement("return _maps.get(name)")
               .build()
         )
         .addType(
            TypeSpec.classBuilder(abstractAssetClassName)
               .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.ABSTRACT)
               .addField(String::class.java, "$", Modifier.PUBLIC, Modifier.FINAL)
               .addField(
                  FieldSpec.builder(
                     ParameterizedTypeName.get(
                        ClassName.get(java.util.Map::class.java),
                        ClassName.get(String::class.java),
                        abstractLayerClassName
                     ), "_layers", Modifier.PROTECTED, Modifier.FINAL
                  )
                     .initializer("new \$T<>()", java.util.HashMap::class.java)
                     .build()
               )
               .addMethod(
                  MethodSpec.constructorBuilder()
                     .addParameter(ClassName.get(String::class.java), "uid")
                     .addStatement("this.\$\$ = uid")
                     .build()
               )
               .addMethod(
                  MethodSpec.methodBuilder("layer")
                     .addModifiers(Modifier.PUBLIC)
                     .addParameter(ClassName.get(String::class.java), "name")
                     .returns(abstractLayerClassName)
                     .addStatement("return this._layers.get(name)")
                     .build()
               )
               .build()
         ).addType(
            TypeSpec.classBuilder(abstractLayerClassName)
               .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.ABSTRACT)
               .addField(TypeName.INT, "$", Modifier.PUBLIC, Modifier.FINAL)
               .addField(
                  FieldSpec.builder(
                     ParameterizedTypeName.get(
                        ClassName.get(java.util.Map::class.java),
                        ClassName.get(String::class.java),
                        MAP_PIN_TYPE
                     ), "_labels", Modifier.PROTECTED, Modifier.FINAL
                  )
                     .initializer("new \$T<>()", java.util.HashMap::class.java)
                     .build()
               )
               .addMethod(
                  MethodSpec.constructorBuilder()
                     .addParameter(TypeName.INT, "index")
                     .addStatement("this.\$\$ = index")
                     .build()
               )
               .addMethod(
                  MethodSpec.methodBuilder("label")
                     .addModifiers(Modifier.PUBLIC)
                     .addParameter(ClassName.get(String::class.java), "label")
                     .returns(MAP_PIN_TYPE)
                     .addStatement("return this._labels.get(label)")
                     .build()
               )
               .build()
         )
         .apply {
            mapLayers.forEach { (asset, layers) ->
               val assetClassName = ClassName.get("", getCapitalizedAssetName(asset))

               addField(
                  FieldSpec
                     .builder(assetClassName, getAssetName(asset), Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                     .initializer("new \$T()", assetClassName)
                     .build()
               )

               addType(
                  TypeSpec.classBuilder(assetClassName)
                     .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                     .superclass(abstractAssetClassName)
                     .addMethod(
                        MethodSpec.constructorBuilder()
                           .addStatement("super(\"${asset.uid}\")")
                           .apply {
                              layers.forEach { layer ->
                                 addStatement("this._layers.put(\"${layer.name}\", this.${getAssetName(layer.name)})")
                              }
                           }
                           .build()
                     )
                     .apply {
                        layers.forEachIndexed { index, layer ->
                           val layerClass = ClassName.get("", getCapitalizedAssetName(layer.name))
                           val type = TypeSpec.classBuilder(layerClass)
                              .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                              .superclass(abstractLayerClassName)
                              .addMethod(
                                 MethodSpec
                                    .constructorBuilder()
                                    .addStatement("super($index)")
                                    .apply {
                                       if (layer is ObjectLayer) {
                                          layer.labels.forEach { label ->
                                             addStatement("this._labels.put(\"${label.label}\", this.${getAssetName(label.label)})")
                                          }
                                       }
                                    }
                                    .build()
                              )
                              //.addField(FieldSpec.builder(TypeName.INT, "$", Modifier.PUBLIC, Modifier.FINAL).initializer(index.toString()).build())
                              .apply {
                                 if (layer is ObjectLayer) {
                                    layer.labels.forEach { label ->
                                       addField(FieldSpec.builder(MAP_PIN_TYPE, getAssetName(label.label), Modifier.FINAL, Modifier.PUBLIC)
                                          .initializer("\$T.builder()" +
                                             ".map(\"${asset.uid}\")" +
                                             ".layer(${index})" +
                                             ".x(${label.x})" +
                                             ".y(${label.y})" +
                                             ".build()", MAP_PIN_TYPE)
                                          .build())
                                    }
                                 }
                              }
                              .build()

                           addType(type)

                           addField(
                              FieldSpec.builder(
                                 layerClass,
                                 getAssetName(layer.name),
                                 Modifier.PUBLIC,
                                 Modifier.FINAL
                              )
                                 .initializer("new \$T()", layerClass)
                                 .build()
                           )
                        }
                     }
                     .build()
               )
            }
         }
         .build()
   }

   private fun getAssetName(asset: Asset) = getAssetName(asset.name)

   private fun getAssetName(name: String) = name
      .split("\\s+".toRegex())
      .joinToString("_") { it.lowercase() }

   private fun getCapitalizedAssetName(asset: Asset) = getCapitalizedAssetName(asset.name)

   private fun getCapitalizedAssetName(name: String) = name
      .split("\\s+".toRegex())
      .joinToString("") { part -> part.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }

   companion object {
      val MAP_PIN_TYPE = ClassName.get("com.bartlomiejpluta.base.api.map.layer.object", "MapPin")
      val GENERATOR_NAME = AssetMapCodeGenerator::class.java.canonicalName
   }
}