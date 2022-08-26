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
         handleTables(project, databaseService.database.tables)
      }
   }

   private fun handleTables(project: Project, tables: List<SchemaTable>) {
      tables.forEach {
         handleTable(project, it)
      }

      generateRootDAO(tables, project)
   }

   private fun generateRootDAO(tables: List<SchemaTable>, project: Project) {
      val generatedAnnotation = AnnotationSpec.builder(Generated::class.java).addMember("value", "\$S", GENERATOR_NAME)
         .addMember("date", "\$S", DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
         .addMember("comments", "\$S", "Collective Data Access Object")
         .build()

      val generatedClass = TypeSpec.classBuilder(ClassName.get(DAO_PACKAGE, "dao"))
         .addAnnotation(generatedAnnotation)
         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
         .apply {
            tables.forEach { table ->
               val type = generateDaoClassName(table)
               addField(
                  FieldSpec.builder(type, table.name.lowercase(), Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                     .initializer("new \$T()", type)
                     .build()
               )
            }
         }
         .build()

      JavaFile
         .builder("DB", generatedClass)
         .build()
         .writeTo(project.buildGeneratedCodeDirectory)
   }

   private fun generateDaoClassName(table: SchemaTable) =
      ClassName.get(DAO_PACKAGE, snakeToPascalCase(table.name) + "DAO")

   private fun handleTable(project: Project, table: SchemaTable) {
      generateModel(project, table)
      generateTableDAO(project, table)
   }

   private fun generateModel(project: Project, table: SchemaTable) {
      val className = generateModelClassName(table)

      val generatedAnnotation = AnnotationSpec.builder(Generated::class.java).addMember("value", "\$S", GENERATOR_NAME)
         .addMember("date", "\$S", DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
         .addMember("comments", "\$S", "Model generated for '${table.name}' database table")
         .build()

      val generatedClass = TypeSpec.classBuilder(className)
         .addAnnotation(DATA_ANNOTATION)
         .addAnnotation(BUILDER_ANNOTATION)
         .addAnnotation(generatedAnnotation)
         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)

      table.columns.forEach {
         generatedClass.addField(dbToJavaType(it), snakeToCamelCase(it.name), Modifier.PRIVATE, Modifier.FINAL)
      }

      JavaFile
         .builder(MODEL_PACKAGE, generatedClass.build())
         .build()
         .writeTo(project.buildGeneratedCodeDirectory)
   }

   private fun generateModelClassName(table: SchemaTable) =
      ClassName.get(MODEL_PACKAGE, "${snakeToPascalCase(table.name)}Model")

   private fun generateTableDAO(project: Project, table: SchemaTable) {
      val className = generateDaoClassName(table)

      val model = generateModelClassName(table)

      val generatedAnnotation = AnnotationSpec.builder(Generated::class.java).addMember("value", "\$S", GENERATOR_NAME)
         .addMember("date", "\$S", DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
         .addMember("comments", "\$S", "Data Access Object generated for '${table.name}' database table")
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
         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
         .addMethod(MethodSpec.constructorBuilder().build())
         .addMethod(
            MethodSpec.methodBuilder("findAll")
               .returns(ParameterizedTypeName.get(ClassName.get(List::class.java), model))
               .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
               .addStatement("var list = new \$T<\$T>()", LinkedList::class.java, model)
               .beginControlFlow("\$T.INSTANCE.getContext().withDatabase(db ->", CONTEXT_HOLDER)
               .apply {
                  val sql = "SELECT * FROM `${table.name}`"
                  addStatement("var statement = db.prepareStatement(\"$sql\")")
               }
               .addStatement("var result = statement.executeQuery()")
               .beginControlFlow("while(result.next())")
               .addStatement("var model = ${model.simpleName()}.builder()")
               .apply {
                  table.columns.forEach { column ->
                     addStatement("model.${snakeToCamelCase(column.name)}(result.${dbToGetMethod(column)}(\"${column.name}\"))")
                  }
               }
               .addStatement("list.add(model.build())")
               .endControlFlow()
               .endControlFlow(")")
               .addStatement("return list")
               .build()
         )
         .addMethod(
            MethodSpec.methodBuilder("find")
               .addParameter(
                  ParameterSpec.builder(dbToJavaType(primaryKey), "id")
                     .apply {
                        if (!dbToJavaType(primaryKey).isPrimitive) {
                           addAnnotation(ClassName.get("lombok", "NonNull"))
                        }
                     }
                     .build()
               )
               .returns(model)
               .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
               .beginControlFlow("return \$T.INSTANCE.getContext().withDatabase(db ->", CONTEXT_HOLDER)
               .apply {
                  val sql = "SELECT * FROM `${table.name}` WHERE `${primaryKey.name}` = ?"
                  addStatement("var statement = db.prepareStatement(\"$sql\")")
               }
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
         .addMethod(
            MethodSpec.methodBuilder("query")
               .addModifiers(Modifier.PUBLIC)
               .returns(QUERY_BUILDER_CLASS)
               .addStatement("return new \$T()", QUERY_BUILDER_CLASS)
               .build()
         )
         .addMethod(
            MethodSpec.methodBuilder("save")
               .addParameter(
                  ParameterSpec.builder(model, "model")
                     .addAnnotation(ClassName.get("lombok", "NonNull"))
                     .build()
               )
               .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
               .beginControlFlow("\$T.INSTANCE.getContext().withDatabase(db ->", CONTEXT_HOLDER)
               .apply {
                  val columns = table.columns.joinToString(", ") { "`${it.name}`" }
                  val values = table.columns.joinToString(", ") { "?" }
                  val sql = "MERGE INTO `${table.name}` ($columns) KEY(${primaryKey.name}) VALUES ($values)"
                  addStatement("var statement = db.prepareStatement(\"$sql\")")
                  table.columns.forEachIndexed { index, column ->
                     val getterPrefix = if (column.type == ColumnType.BOOLEAN) "is" else "get"
                     addStatement(
                        "statement.${dbToBindMethod(column)}(${index + 1}, model.${getterPrefix}${
                           snakeToPascalCase(
                              column.name
                           )
                        }())"
                     )
                  }
               }
               .addStatement("statement.execute()")
               .endControlFlow(")")
               .build()
         )
         .addMethod(
            MethodSpec.methodBuilder("insert")
               .addParameter(
                  ParameterSpec.builder(model, "model")
                     .addAnnotation(ClassName.get("lombok", "NonNull"))
                     .build()
               )
               .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
               .beginControlFlow("\$T.INSTANCE.getContext().withDatabase(db ->", CONTEXT_HOLDER)
               .apply {
                  val columns = table.columns.joinToString(", ") { "`${it.name}`" }
                  val values = table.columns.joinToString(", ") { "?" }
                  val sql = "INSERT INTO `${table.name}` ($columns) VALUES ($values)"
                  addStatement("var statement = db.prepareStatement(\"$sql\")")
                  table.columns.forEachIndexed { index, column ->
                     val getterPrefix = if (column.type == ColumnType.BOOLEAN) "is" else "get"
                     addStatement(
                        "statement.${dbToBindMethod(column)}(${index + 1}, model.${getterPrefix}${
                           snakeToPascalCase(
                              column.name
                           )
                        }())"
                     )
                  }
               }
               .addStatement("statement.execute()")
               .endControlFlow(")")
               .build()
         )
         .addMethod(
            MethodSpec.methodBuilder("update")
               .addParameter(
                  ParameterSpec.builder(model, "model")
                     .addAnnotation(ClassName.get("lombok", "NonNull"))
                     .build()
               )
               .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
               .beginControlFlow("\$T.INSTANCE.getContext().withDatabase(db ->", CONTEXT_HOLDER)
               .apply {
                  val fields = table.columns
                     .filter { it != primaryKey }
                     .joinToString(", ") { "`${it.name}`=?" }
                  val sql = "UPDATE `${table.name}` SET $fields WHERE `${primaryKey.name}`=?"
                  addStatement("var statement = db.prepareStatement(\"$sql\")")
                  table.columns
                     .filter { it != primaryKey }
                     .forEachIndexed { index, column ->
                        val getterPrefix = if (column.type == ColumnType.BOOLEAN) "is" else "get"
                        addStatement(
                           "statement.${dbToBindMethod(column)}(${index + 1}, model.${getterPrefix}${
                              snakeToPascalCase(
                                 column.name
                              )
                           }())"
                        )
                     }
                  val getterPrefix = if (primaryKey.type == ColumnType.BOOLEAN) "is" else "get"
                  addStatement(
                     "statement.${dbToBindMethod(primaryKey)}(${table.columns.size}, model.${getterPrefix}${
                        snakeToPascalCase(
                           primaryKey.name
                        )
                     }())"
                  )
               }
               .addStatement("statement.execute()")
               .endControlFlow(")")
               .build()
         )
         .addType(TypeSpec.enumBuilder(COLUMN_ENUM)
            .addAnnotation(REQUIRED_ARGS_CONSTRUCTOR_ANNOTATION)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .apply {
               table.columns.forEach {
                  addEnumConstant(
                     it.name.uppercase(),
                     TypeSpec.anonymousClassBuilder("\$S", it.name).build()
                  )
               }
            }
            .addField(String::class.java, "column", Modifier.PRIVATE, Modifier.FINAL)
            .build())
         .addType(
            TypeSpec.classBuilder(QUERY_FILTER_CLASS)
               .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
               .addAnnotation(REQUIRED_ARGS_CONSTRUCTOR_ANNOTATION)
               .addField(COLUMN_ENUM, "column", Modifier.PRIVATE, Modifier.FINAL)
               .addField(RELOP_ENUM, "op", Modifier.PRIVATE, Modifier.FINAL)
               .addField(Object::class.java, "value", Modifier.PRIVATE, Modifier.FINAL)
               .build()
         )
         .addType(
            TypeSpec.classBuilder(QUERY_BUILDER_CLASS)
               .addAnnotation(PRIVATE_REQUIRED_ARGS_CONSTRUCTOR_ANNOTATION)
               .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
               .addField(
                  FieldSpec.builder(
                     ParameterizedTypeName.get(ClassName.get(List::class.java), QUERY_FILTER_CLASS),
                     "filters",
                     Modifier.PRIVATE,
                     Modifier.FINAL
                  )
                     .initializer("new \$T<>()", LinkedList::class.java)
                     .build()
               )
               .addField(
                  FieldSpec.builder(
                     ParameterizedTypeName.get(ClassName.get(List::class.java), TypeName.get(String::class.java)),
                     "ordering",
                     Modifier.PRIVATE,
                     Modifier.FINAL
                  )
                     .initializer("new \$T<>()", LinkedList::class.java)
                     .build()
               )
               .addMethod(
                  MethodSpec.methodBuilder("where")
                     .addModifiers(Modifier.PUBLIC)
                     .addParameter(COLUMN_ENUM, "column")
                     .addParameter(RELOP_ENUM, "op")
                     .addParameter(Object::class.java, "value")
                     .returns(QUERY_BUILDER_CLASS)
                     .addStatement("this.filters.add(new \$T(column, op, value))", QUERY_FILTER_CLASS)
                     .addStatement("return this")
                     .build()
               )
               .addMethod(
                  MethodSpec.methodBuilder("orderBy")
                     .addModifiers(Modifier.PUBLIC)
                     .addParameter(COLUMN_ENUM, "column")
                     .addParameter(ORDERING_ENUM, "order")
                     .returns(QUERY_BUILDER_CLASS)
                     .addStatement("this.ordering.add(String.format(\"`%s %s\", column.column, order.getOrdering()))")
                     .addStatement("return this")
                     .build()
               )
               .addMethod(
                  MethodSpec.methodBuilder("orderBy")
                     .addModifiers(Modifier.PUBLIC)
                     .addParameter(String::class.java, "function")
                     .addParameter(ORDERING_ENUM, "order")
                     .returns(QUERY_BUILDER_CLASS)
                     .addStatement("this.ordering.add(String.format(\"`%s %s\", function, order.getOrdering()))")
                     .addStatement("return this")
                     .build()
               )
               .addMethod(
                  MethodSpec.methodBuilder("orderBy")
                     .addModifiers(Modifier.PUBLIC)
                     .addParameter(String::class.java, "function")
                     .returns(QUERY_BUILDER_CLASS)
                     .addStatement("this.ordering.add(function)")
                     .addStatement("return this")
                     .build()
               )
               .addMethod(
                  MethodSpec.methodBuilder("find")
                     .addModifiers(Modifier.PUBLIC)
                     .returns(ParameterizedTypeName.get(ClassName.get(List::class.java), model))
                     .addStatement("var list = new \$T<\$T>()", LinkedList::class.java, model)
                     .beginControlFlow("\$T.INSTANCE.getContext().withDatabase(db ->", CONTEXT_HOLDER)
                     .addStatement(
                        "var filter = filters.stream().map(f -> String.format(\"`%s` %s ?\", f.column.column, f.op.getOp())).collect(\$T.joining(\" AND \"))",
                        COLLECTORS_CLASS
                     )
                     .addStatement("var order = ordering.isEmpty() ? \"\" : \" ORDER BY \" + ordering.stream().collect(\$T.joining(\", \"))", COLLECTORS_CLASS)
                     .apply {
                        val sql = "SELECT * FROM `${table.name}` WHERE "
                        addStatement("var statement = db.prepareStatement(\"$sql\" + filter + order)")
                     }
                     .addStatement("var i = 1")
                     .beginControlFlow("for (var f : filters)")
                     .addStatement("statement.setObject(i++, f.value)")
                     .endControlFlow()
                     .addStatement("var result = statement.executeQuery()")
                     .beginControlFlow("while(result.next())")
                     .addStatement("var model = ${model.simpleName()}.builder()")
                     .apply {
                        table.columns.forEach { column ->
                           addStatement("model.${snakeToCamelCase(column.name)}(result.${dbToGetMethod(column)}(\"${column.name}\"))")
                        }
                     }
                     .addStatement("list.add(model.build())")
                     .endControlFlow()
                     .endControlFlow(")")
                     .addStatement("return list")
                     .build()
               )
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
      private const val MODEL_PACKAGE = "DB.model"
      private const val DAO_PACKAGE = "DB"

      private val BUILDER_ANNOTATION = ClassName.get("lombok", "Builder")
      private val DATA_ANNOTATION = ClassName.get("lombok", "Data")
      private val REQUIRED_ARGS_CONSTRUCTOR_ANNOTATION = ClassName.get("lombok", "RequiredArgsConstructor")
      private val PRIVATE_REQUIRED_ARGS_CONSTRUCTOR_ANNOTATION =
         AnnotationSpec.builder(ClassName.get("lombok", "RequiredArgsConstructor"))
            .addMember("access", "\$T.PRIVATE", ClassName.get("lombok", "AccessLevel"))
            .build()
      private val CONTEXT_HOLDER = ClassName.get("com.bartlomiejpluta.base.api.context", "ContextHolder")

      private val COLUMN_ENUM = ClassName.get("", "Column")
      private val RELOP_ENUM = ClassName.get("com.bartlomiejpluta.base.lib.db", "Relop")
      private val ORDERING_ENUM = ClassName.get("com.bartlomiejpluta.base.lib.db", "Ordering")
      private val QUERY_FILTER_CLASS = ClassName.get("", "QueryFilter")
      private val QUERY_BUILDER_CLASS = ClassName.get("", "QueryBuilder")
      private val COLLECTORS_CLASS = ClassName.get("java.util.stream", "Collectors")
   }
}