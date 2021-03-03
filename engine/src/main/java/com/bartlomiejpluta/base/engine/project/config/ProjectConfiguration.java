package com.bartlomiejpluta.base.engine.project.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.project")
public class ProjectConfiguration {
   private String mainFile;
   private String resourcePath;


   public String projectFile(String... path) {
      return Path.of(resourcePath, path).toString().replaceAll("\\\\", "/");
   }
}