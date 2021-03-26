package com.bartlomiejpluta.base.editor.database.source

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.io.File
import java.sql.Connection

class DataSource(dbFile: File) {
   private val config = HikariConfig()
   private val source: HikariDataSource

   init {
      config.jdbcUrl = "jdbc:h2:file:${dbFile.absolutePath.replace("\\", "/")}"
      source = HikariDataSource(config)
   }

   val connection: Connection
      get() = source.connection

   fun close() = source.close()
}