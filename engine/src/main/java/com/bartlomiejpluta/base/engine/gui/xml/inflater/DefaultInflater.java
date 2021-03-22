package com.bartlomiejpluta.base.engine.gui.xml.inflater;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.engine.error.AppException;
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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@org.springframework.stereotype.Component
public class DefaultInflater implements Inflater {
   private static final String[] IMPORTS = Stream.of(GUI.class, SizeMode.class, WindowPosition.class)
         .map(Class::getCanonicalName)
         .toArray(String[]::new);

   private final DocumentBuilder builder;
   private final ClassLoader loader;

   @Autowired
   @SneakyThrows
   public DefaultInflater(ClassLoader loader) {
      this.loader = loader;

      var factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      this.builder = factory.newDocumentBuilder();
   }

   @Override
   @SneakyThrows
   public Component inflateComponent(String xml, Context context, GUI gui) {
      var document = builder.parse(xml);
      return inflateComponent(document.getDocumentElement(), context, gui);
   }

   @Override
   @SneakyThrows
   public Component inflateComponent(File file, Context context, GUI gui) {
      var document = builder.parse(file);
      return inflateComponent(document.getDocumentElement(), context, gui);
   }

   @Override
   @SneakyThrows
   public Component inflateComponent(InputStream is, Context context, GUI gui) {
      var document = builder.parse(is);
      return inflateComponent(document.getDocumentElement(), context, gui);
   }

   @Override
   @SneakyThrows
   public Window inflateWindow(String xml, Context context, GUI gui) {
      var document = builder.parse(xml);
      return inflateWindow(document.getDocumentElement(), context, gui);
   }

   @Override
   @SneakyThrows
   public Window inflateWindow(File file, Context context, GUI gui) {
      var document = builder.parse(file);
      return inflateWindow(document.getDocumentElement(), context, gui);
   }

   @Override
   @SneakyThrows
   public Window inflateWindow(InputStream is, Context context, GUI gui) {
      var document = builder.parse(is);
      return inflateWindow(document.getDocumentElement(), context, gui);
   }

   @SneakyThrows
   private Window inflateWindow(Node root, Context context, GUI gui) {
      var refs = new HashMap<String, Component>();

      var uri = root.getNamespaceURI();
      var name = root.getLocalName();

      if (uri != null) {
         name = uri + "." + name;
      }

      var canonicalName = name
            .replaceAll("\\*", "")
            .replaceAll("\\.+", ".")
            .replaceAll("-+", "\\$");

      var windowClass = loader.loadClass(canonicalName);

      var window = (Window) windowClass.getConstructor(Context.class, GUI.class).newInstance(context, gui);
      var attributes = root.getAttributes();

      // Set attributes via setter methods
      for (int i = 0; i < attributes.getLength(); ++i) {
         var attribute = attributes.item(i);

         // Ignore namespaces definitions
         if ("xmlns".equals(attribute.getPrefix())) {
            continue;
         }

         var setterName = createSetterMethodName(attribute.getLocalName());
         var value = parseValue(attribute.getNodeValue());
         var setter = windowClass.getMethod(setterName, value.getClass());
         setter.invoke(window, value);
      }

      // Parse window content
      var children = root.getChildNodes();

      boolean hasContent = false;

      for (int i = 0; i < children.getLength(); ++i) {
         var child = children.item(i);

         if (child.getNodeType() != Node.ELEMENT_NODE) {
            continue;
         }

         if (hasContent) {
            throw new AppException("Window can have at most 1 child");
         }

         var content = parseNode(child, refs, context, gui);
         window.setContent(content);
         hasContent = true;
      }

      updateWidgetRefs(window, refs);

      invokeInflatableHook(window);

      return window;
   }

   private void invokeInflatableHook(Widget widget) {
      if (widget instanceof Inflatable) {
         ((Inflatable) widget).onInflate();
      }
   }

   private Component inflateComponent(Node root, Context context, GUI gui) {
      var refs = new HashMap<String, Component>();
      var component = parseNode(root, refs, context, gui);

      updateWidgetRefs(component, refs);
      invokeInflatableHook(component);

      return component;
   }

   @SneakyThrows
   private void updateWidgetRefs(Widget widget, Map<String, Component> refs) {
      var widgetClass = widget.getClass();
      for (var field : widgetClass.getDeclaredFields()) {
         if (field.isAnnotationPresent(Ref.class)) {
            var ref = field.getAnnotation(Ref.class).value();
            var referencedComponent = refs.get(ref);

            if (referencedComponent == null) {
               throw new AppException("The [%s] widget is trying to reference component [%s] which does not exist", widgetClass.getSimpleName(), ref);
            }

            field.setAccessible(true);
            field.set(widget, referencedComponent);
         }
      }
   }

   @SneakyThrows
   private Component parseNode(Node node, Map<String, Component> refs, Context context, GUI gui) {
      var uri = node.getNamespaceURI();
      var name = node.getLocalName();

      if (uri != null) {
         name = uri + "." + name;
      }

      var canonicalName = name
            .replaceAll("\\*", "")
            .replaceAll("\\.+", ".")
            .replaceAll("-+", "\\$");

      var componentClass = loader.loadClass(canonicalName);
      var component = createComponent(componentClass, node.getAttributes(), refs, context, gui);

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

         component.add(parseNode(childNode, refs, context, gui));
      }

      return component;
   }

   @SneakyThrows
   private Component createComponent(Class<?> componentClass, NamedNodeMap attributes, Map<String, Component> refs, Context context, GUI gui) {
      var component = (Component) componentClass.getConstructor(Context.class, GUI.class).newInstance(context, gui);

      // Set attributes via setter methods
      for (int i = 0; i < attributes.getLength(); ++i) {
         var attribute = attributes.item(i);

         // Ignore namespaces definitions
         if ("xmlns".equals(attribute.getPrefix())) {
            continue;
         }

         // Collect referencable components
         if ("ref".equals(attribute.getLocalName())) {
            refs.put(attribute.getNodeValue(), component);
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
