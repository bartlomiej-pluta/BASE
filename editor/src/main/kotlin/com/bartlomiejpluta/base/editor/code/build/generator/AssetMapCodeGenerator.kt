package com.bartlomiejpluta.base.editor.code.build.generator

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.map.serial.MapDeserializer
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.project.model.Project
import com.squareup.javapoet.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
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
      val className = ClassName.get("A", name)
      return TypeSpec
         .classBuilder(className)
         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
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
         .addField(String::class.java, "uid", Modifier.PUBLIC, Modifier.FINAL)
         .addMethod(
            MethodSpec
               .constructorBuilder()
               .addModifiers(Modifier.PRIVATE)
               .addParameter(TypeName.get(String::class.java), "uid")
               .addStatement("this.uid = uid")
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
      val className = ClassName.get("A", name)
      val mapLayers = assets
         .map { asset -> asset to mapDeserializer.deserialize(asset.file.inputStream()) }
         .associate { (asset, map) -> asset to map.layers }

      val abstractAssetClassName = ClassName.get("", "GameMapAsset")

      return TypeSpec
         .classBuilder(className)
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
         .addField(
            FieldSpec.builder(
               ParameterizedTypeName.get(
                  ClassName.get(java.util.Map::class.java),
                  ClassName.get(String::class.java),
                  ClassName.get(Integer::class.java)
               ), "_layers", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL
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
            .apply {
               mapLayers.forEach { asset, layers ->
                  layers.forEach { layer ->
                     addStatement(
                        "_layers.put(\"${asset.name}::${layer.name}\", ${getAssetName(asset)}.layers.${
                           getAssetName(
                              layer.name
                           )
                        })"
                     )
                  }
               }
            }
            .build()
         )
         .addMethod(
            MethodSpec
               .methodBuilder("get")
               .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
               .returns(abstractAssetClassName)
               .addParameter(TypeName.get(String::class.java), "name")
               .addStatement("return _maps.get(name)")
               .build()
         )
         .addMethod(
            MethodSpec
               .methodBuilder("getLayer")
               .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
               .returns(ClassName.get(Integer::class.java))
               .addParameter(TypeName.get(String::class.java), "mapName")
               .addParameter(TypeName.get(String::class.java), "layerName")
               .addStatement("return _layers.get(mapName + \"::\" + layerName)")
               .build()
         )
         .addType(
            TypeSpec.classBuilder(abstractAssetClassName)
               .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.ABSTRACT)
               .addField(String::class.java, "uid", Modifier.PUBLIC, Modifier.FINAL)
               .addField(
                  FieldSpec.builder(
                     ParameterizedTypeName.get(
                        ClassName.get(java.util.Map::class.java),
                        ClassName.get(String::class.java),
                        ClassName.get(Integer::class.java)
                     ), "_layers", Modifier.PROTECTED, Modifier.FINAL
                  )
                     .initializer("new \$T<>()", java.util.HashMap::class.java)
                     .build()
               )
               .addMethod(
                  MethodSpec.constructorBuilder()
                     .addParameter(ClassName.get(String::class.java), "uid")
                     .addStatement("this.uid = uid")
                     .build()
               )
               .addMethod(
                  MethodSpec.methodBuilder("get")
                     .addModifiers(Modifier.PUBLIC)
                     .addParameter(ClassName.get(String::class.java), "name")
                     .returns(ClassName.get(Integer::class.java))
                     .addStatement("return this._layers.get(name)")
                     .build()
               )
               .build()
         )
         .apply {
            mapLayers.forEach { (asset, layers) ->
               val assetClassName = ClassName.get("", "GameMapAsset_${getCapitalizedAssetName(asset)}")
               val layersClassName = ClassName.get("", "GameMapAsset_Layers_${getCapitalizedAssetName(asset)}")

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
                     .addField(
                        FieldSpec.builder(layersClassName, "layers", Modifier.PUBLIC, Modifier.FINAL)
                           .initializer("new \$T()", layersClassName)
                           .build()
                     )
                     .addMethod(
                        MethodSpec.constructorBuilder()
                           .addStatement("super(\"${asset.uid}\")")
                           .apply {
                              layers.forEach { layer ->
                                 addStatement("this._layers.put(\"${layer.name}\", layers.${getAssetName(layer.name)})")
                              }
                           }
                           .build()
                     )
                     .build()
               )
               addType(
                  TypeSpec.classBuilder(layersClassName)
                     .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                     .apply {
                        layers.forEachIndexed { index, layer ->
                           addField(
                              FieldSpec.builder(
                                 TypeName.INT,
                                 getAssetName(layer.name),
                                 Modifier.PUBLIC,
                                 Modifier.FINAL
                              )
                                 .initializer(index.toString())
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
}