package com.bartlomiejpluta.base.editor.database.source

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.h2.tools.RunScript
import org.h2.tools.Script
import java.io.File
import java.sql.Connection

class H2DBDataSource(dbFile: File) {
   private val config = HikariConfig()
   private val source: HikariDataSource

   init {
      config.jdbcUrl = "jdbc:h2:mem:data;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1"
      source = HikariDataSource(config)
      source.connection.use {
         RunScript.execute(it, dbFile.reader())
      }
   }

   val connection: Connection
      get() = source.connection

   fun close() = source.close()
}