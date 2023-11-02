package com.bartlomiejpluta.base.util.proto;

import com.bartlomiejpluta.base.proto.GameMapProto;
import com.bartlomiejpluta.base.proto.ProjectProto;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import lombok.SneakyThrows;

import java.io.*;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class MigrationUtil {
   private static final String JSON_SUFFIX = ".json";

   private final String root;

   public MigrationUtil(String root) {
      this.root = root;
   }

   public static void main(String[] args) {

   }

   public void exportAll() {
      exportMaps();
      exportProject();
   }

   public void importAll() {
      importMaps();
      importProject();
   }

   private void exportMaps() {
      var directory = new File(root, "maps");

      for (var map : requireNonNull(directory.listFiles())) {
         if(map.getName().endsWith(".dat")) {
            toJson(map.getAbsolutePath(), map.getAbsolutePath() + JSON_SUFFIX, this::parseMap);
         }
      }
   }

   private void exportProject() {
      var project = new File(root, "project.bep");
      toJson(project.getAbsolutePath(), project.getAbsolutePath() + JSON_SUFFIX, this::parseProject);
   }

   private void importMaps() {
      var directory = new File(root, "maps");

      for (var map : requireNonNull(directory.listFiles())) {
         if(map.getName().endsWith(JSON_SUFFIX)) {
            var path = map.getAbsolutePath();
            fromJson(path, path.substring(0, path.length() - JSON_SUFFIX.length()), GameMapProto.GameMap.newBuilder());
         }
      }
   }

   private void importProject() {
      var project = new File(root, "project.bep" + JSON_SUFFIX);
      var path = project.getAbsolutePath();
      fromJson(path, path.substring(0, path.length() - JSON_SUFFIX.length()), ProjectProto.Project.newBuilder());
   }

   @SneakyThrows
   private MessageOrBuilder parseMap(InputStream is) {
      return GameMapProto.GameMap.parseFrom(is);
   }

   @SneakyThrows
   private MessageOrBuilder parseProject(InputStream is) {
      return ProjectProto.Project.parseFrom(is);
   }

   @SneakyThrows
   private void toJson(String input, String output, Function<InputStream, MessageOrBuilder> parser) {
      try (var is = new FileInputStream(input); var writer = new PrintWriter(new FileWriter(output))) {
         var model = parser.apply(is);
         var json = JsonFormat.printer().print(model);
         json.lines().forEach(writer::println);
      }
   }

   @SneakyThrows
   private void fromJson(String input, String output, Message.Builder builder) {
      try (var reader = new FileReader(input); var os = new FileOutputStream(output)) {
         JsonFormat.parser().ignoringUnknownFields().merge(reader, builder);
         builder.build().writeTo(os);
      }
   }
}
