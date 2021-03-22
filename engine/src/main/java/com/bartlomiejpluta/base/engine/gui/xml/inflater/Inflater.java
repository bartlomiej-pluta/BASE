package com.bartlomiejpluta.base.engine.gui.xml.inflater;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.gui.Window;

import java.io.File;
import java.io.InputStream;

public interface Inflater {
   Component inflateComponent(String xml, Context context, GUI gui);

   Component inflateComponent(File file, Context context, GUI gui);

   Component inflateComponent(InputStream is, Context context, GUI gui);

   Window inflateWindow(String xml, Context context, GUI gui);

   Window inflateWindow(File file, Context context, GUI gui);

   Window inflateWindow(InputStream is, Context context, GUI gui);
}
