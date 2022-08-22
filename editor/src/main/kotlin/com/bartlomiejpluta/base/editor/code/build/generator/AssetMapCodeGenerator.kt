package com.bartlomiejpluta.base.editor.code.build.generator

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.project.model.Project
import com.squareup.javapoet.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.lang.model.element.Modifier

@Component
class AssetMapCodeGenerator : CodeGenerator {

   @Autowired
   private lateinit var projectContext: ProjectContext

   override fun generate() {
      projectContext.project?.let(::generateAssetClasses)
   }

   private fun generateAssetClasses(project: Project) {
      listOf(
         generateAssetClass("maps", project.maps),
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
            FieldSpec.builder(ParameterizedTypeName.get(
               ClassName.get(java.util.Map::class.java),
               ClassName.get(String::class.java),
               className
            ), "_map", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
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

   private fun getAssetName(asset: Asset) = asset.name
      .split("\\s+".toRegex())
      .joinToString("_") { it.lowercase() }

}