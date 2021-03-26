package com.bartlomiejpluta.base.engine.database.service;

import com.bartlomiejpluta.base.engine.project.config.ProjectConfiguration;
import com.bartlomiejpluta.base.engine.util.res.ResourcesManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.RunScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.sql.Connection;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class H2DatabaseService implements DatabaseService {
   private static final String CONNECTION_STRING = "jdbc:h2:mem:main";
   private final ProjectConfiguration configuration;
   private final ResourcesManager resourcesManager;
   private HikariDataSource source;

   @SneakyThrows
   @Override
   public void init() {
      log.info("Starting in-memory database");
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(CONNECTION_STRING);
      this.source = new HikariDataSource(config);

      log.info("Loading data into in-memory database");
      var data = resourcesManager.loadResourceAsStream(configuration.getDatabaseScriptFile());
      RunScript.execute(getConnection(), new InputStreamReader(data));
   }

   @SneakyThrows
   @Override
   public Connection getConnection() {
      return source.getConnection();
   }

   @Override
   public void cleanUp() {
      log.info("Closing in-memory database connection pool");
      source.close();
   }
}
