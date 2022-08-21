package com.bartlomiejpluta.base.editor.code.build.generator

import com.bartlomiejpluta.base.editor.database.model.schema.ColumnType
import com.bartlomiejpluta.base.editor.database.model.schema.SchemaColumn
import com.bartlomiejpluta.base.editor.database.model.schema.SchemaTable
import com.bartlomiejpluta.base.editor.database.service.DatabaseService
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
class DataAccessObjectCodeGenerator : CodeGenerator {

   @Autowired
   private lateinit var projectContext: ProjectContext

   @Autowired
   private lateinit var databaseService: DatabaseService

   override fun generate() {
      projectContext.project?.let { project ->
         databaseService.database.tables.forEach {
            handleTable(project, it)
         }
      }
   }

   private fun handleTable(project: Project, table: SchemaTable) {
      val model = generateModel(project, table)
      generateDAO(project, table, model)
   }

   private fun generateModel(project: Project, table: SchemaTable): ClassName {
      val className = ClassName.get(MODEL_PACKAGE, "${snakeToPascalCase(table.name)}Model")

      val builderAnnotation = AnnotationSpec
         .builder(ClassName.get("lombok", "Builder"))
         .build()

      val dataAnnotation = AnnotationSpec
         .builder(ClassName.get("lombok", "Data"))
         .build()

      val generatedAnnotation = AnnotationSpec.builder(Generated::class.java).addMember("value", "\$S", GENERATOR_NAME)
         .addMember("date", "\$S", DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
         .addMember("comments", "\$S", "Model generated for '${table.name}' database table")
         .build()

      val generatedClass = TypeSpec.classBuilder(className)
         .addAnnotation(dataAnnotation)
         .addAnnotation(builderAnnotation)
         .addAnnotation(generatedAnnotation)
         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)

      table.columns.forEach {
         generatedClass.addField(dbToJavaType(it), snakeToCamelCase(it.name), Modifier.PRIVATE, Modifier.FINAL)
      }

      JavaFile
         .builder(MODEL_PACKAGE, generatedClass.build())
         .build()
         .writeTo(project.buildGeneratedCodeDirectory)

      return className
   }

   private fun generateDAO(project: Project, table: SchemaTable, model: ClassName) {
      val packageName = "com.bartlomiejpluta.base.generated.db.dao"
      val className = ClassName.get(packageName, "${snakeToPascalCase(table.name)}DAO")

      val generatedAnnotation = AnnotationSpec.builder(Generated::class.java).addMember("value", "\$S", GENERATOR_NAME)
         .addMember("date", "\$S", DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
         .addMember("comments", "\$S", "Data Access Object generated for '${table.name}' database table")
         .build()

      val requiredArgsConstructorAnnotation = AnnotationSpec
         .builder(ClassName.get("lombok", "RequiredArgsConstructor"))
         .build()

      val primaryKeys = table.columns.filter { it.primary }

      if (primaryKeys.isEmpty()) {
         throw IllegalStateException("Table '${table.name}' does not define any primary key")
      }

      if (primaryKeys.size > 1) {
         throw IllegalStateException("Table '${table.name}' defines ${primaryKeys.size} primary keys, whereas only 1 is allowed")
      }

      val primaryKey = primaryKeys[0]

      val generatedClass = TypeSpec.classBuilder(className)
         .addAnnotation(generatedAnnotation)
         .addAnnotation(requiredArgsConstructorAnnotation)
         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
         .addField(
            ClassName.get("com.bartlomiejpluta.base.api.context", "Context"),
            "context",
            Modifier.PRIVATE,
            Modifier.FINAL
         )
         .addMethod(
            MethodSpec.methodBuilder("find")
               .addParameter(dbToJavaType(primaryKey), "id")
               .returns(model)
               .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
               .beginControlFlow("return context.withDatabase(db ->")
               .addStatement("var statement = db.prepareStatement(\"SELECT * FROM `${table.name}` WHERE `${primaryKey.name}` = ?\")")
               .addStatement("statement.${dbToBindMethod(primaryKey)}(1, id)")
               .addStatement("var result = statement.executeQuery()")
               .beginControlFlow("if(result.next())")
               .addStatement("var model = ${model.simpleName()}.builder()")
               .apply {
                  table.columns.forEach { column ->
                     addStatement("model.${snakeToCamelCase(column.name)}(result.${dbToGetMethod(column)}(\"${column.name}\"))")
                  }
               }
               .addStatement("return model.build()")
               .endControlFlow()
               .addStatement("throw new RuntimeException(\"No [${model.simpleName()}] found with [${primaryKey.name}] == [\" + id + \"]\")")
               .endControlFlow(")")
               .build()
         )
         .build()

      JavaFile
         .builder(DAO_PACKAGE, generatedClass)
         .build()
         .writeTo(project.buildGeneratedCodeDirectory)

   }

   private fun snakeToPascalCase(snake: String) = snake
      .lowercase()
      .split(Regex("_+"))
      .joinToString("") { w -> w.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }

   private fun snakeToCamelCase(snake: String) = snakeToPascalCase(snake).replaceFirstChar(Char::lowercase)

   private fun dbToJavaType(column: SchemaColumn) = (when (column.type) {
      ColumnType.INTEGER -> TypeName.INT
      ColumnType.BOOLEAN -> TypeName.BOOLEAN
      ColumnType.TINYINT -> TypeName.SHORT
      ColumnType.SMALLINT -> TypeName.SHORT
      ColumnType.BIGINT -> TypeName.LONG
      ColumnType.DECIMAL -> TypeName.FLOAT
      ColumnType.DOUBLE -> TypeName.DOUBLE
      ColumnType.REAL -> TypeName.FLOAT
      ColumnType.CHAR -> TypeName.CHAR
      else -> TypeName.get(String::class.java)
   }).let { if (column.nullable) it.box() else it }

   private fun dbToBindMethod(column: SchemaColumn) = when (column.type) {
      ColumnType.INTEGER -> "setInt"
      ColumnType.BOOLEAN -> "setBoolean"
      ColumnType.TINYINT -> "setShort"
      ColumnType.SMALLINT -> "setShort"
      ColumnType.BIGINT -> "setLong"
      ColumnType.DECIMAL -> "setFloat"
      ColumnType.DOUBLE -> "setDouble"
      ColumnType.REAL -> "setFloat"
      else -> "setString"
   }

   private fun dbToGetMethod(column: SchemaColumn) = when (column.type) {
      ColumnType.INTEGER -> "getInt"
      ColumnType.BOOLEAN -> "getBoolean"
      ColumnType.TINYINT -> "getShort"
      ColumnType.SMALLINT -> "getShort"
      ColumnType.BIGINT -> "getLong"
      ColumnType.DECIMAL -> "getFloat"
      ColumnType.DOUBLE -> "getDouble"
      ColumnType.REAL -> "getFloat"
      else -> "getString"
   }

   companion object {
      private val GENERATOR_NAME = DataAccessObjectCodeGenerator::class.java.canonicalName
      private const val MODEL_PACKAGE = "com.bartlomiejpluta.base.generated.db.model"
      private const val DAO_PACKAGE = "com.bartlomiejpluta.base.generated.db.dao"
   }
}