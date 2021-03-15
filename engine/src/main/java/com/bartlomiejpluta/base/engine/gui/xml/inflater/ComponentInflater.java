package com.bartlomiejpluta.base.engine.gui.xml.inflater;

import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.gui.base.SizeMode;
import com.bartlomiejpluta.base.api.game.gui.component.Component;
import com.bartlomiejpluta.base.engine.util.reflection.ClassLoader;
import lombok.SneakyThrows;
import org.codehaus.janino.ExpressionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.util.stream.Stream;

@org.springframework.stereotype.Component
public class ComponentInflater {
   private static final String[] IMPORTS = Stream.of(GUI.class, SizeMode.class).map(Class::getCanonicalName).toArray(String[]::new);
   private final DocumentBuilder builder;
   private final ClassLoader loader;

   @Autowired
   @SneakyThrows
   public ComponentInflater(ClassLoader loader) {
      this.loader = loader;

      var factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      this.builder = factory.newDocumentBuilder();
   }

   @SneakyThrows
   public Component inflate(String xml) {
      var document = builder.parse(xml);
      return parseNode(document.getDocumentElement());
   }

   @SneakyThrows
   public Component inflate(File file) {
      var document = builder.parse(file);
      return parseNode(document.getDocumentElement());
   }

   @SneakyThrows
   public Component inflate(InputStream is) {
      var document = builder.parse(is);
      return parseNode(document.getDocumentElement());
   }

   @SneakyThrows
   private Component parseNode(Node node) {
      var uri = node.getNamespaceURI();
      var name = node.getLocalName();

      if (uri != null) {
         name = uri + "." + name;
      }

      var canonicalName = name.replaceAll("\\*", "").replaceAll("\\.+", ".");

      var componentClass = loader.loadClass(canonicalName);
      var component = createComponent(componentClass, node.getAttributes());

      var children = node.getChildNodes();

      // The component is text-like node (e.g. <Label>Some text</Label>)
      if (children.getLength() == 1) {
         var child = children.item(0);
         if (child.getNodeType() == Node.TEXT_NODE) {
            var textSetter = componentClass.getMethod("setText", String.class);
            textSetter.invoke(component, child.getNodeValue());
            return component;
         }
      }

      // The component has non-text children (e.g. <HLayout><Label>Some text</Label></HLayout>)
      for (int i = 0; i < children.getLength(); ++i) {
         var childNode = children.item(i);

         if (childNode.getNodeType() != Node.ELEMENT_NODE) {
            continue;
         }

         component.add(parseNode(childNode));
      }

      return component;
   }

   @SneakyThrows
   private Component createComponent(Class<?> componentClass, NamedNodeMap attributes) {
      var component = (Component) componentClass.getConstructor().newInstance();

      // Set attributes via setter methods
      for (int i = 0; i < attributes.getLength(); ++i) {
         var attribute = attributes.item(i);

         // Ignore namespaces definitions
         if ("xmlns".equals(attribute.getPrefix())) {
            continue;
         }

         var setterName = createSetterMethodName(attribute.getLocalName());
         var value = parseValue(attribute.getNodeValue());
         var setter = componentClass.getMethod(setterName, value.getClass());
         setter.invoke(component, value);
      }

      return component;
   }

   private static String createSetterMethodName(String name) {
      return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
   }

   @SneakyThrows
   private Object parseValue(String value) {
      var evaluator = new ExpressionEvaluator();
      evaluator.setDefaultImports(IMPORTS);
      evaluator.cook(value);
      return evaluator.evaluate();
   }
}
