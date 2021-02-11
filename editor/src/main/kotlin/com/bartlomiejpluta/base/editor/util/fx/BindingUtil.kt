package com.bartlomiejpluta.base.editor.util.fx

import javafx.beans.binding.Bindings
import javafx.collections.MapChangeListener
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import tornadofx.observableListOf

object BindingUtil {
   fun <K, V> observableMapValues(map: ObservableMap<out K, out V>) = observableListOf<V>().apply {
      addAll(map.values)

      // FIXME:
      // It's not really efficient way to track map updates by clearing all
      // and putting it all over again, however it preserves the original map order.
      // The efficiency should be sufficient for the project purposes (there are no any expectations
      // to store a very large collections in here - if the need arose it should be immediately improved).
      map.addListener(MapChangeListener {
         clear()
         addAll(map.values)
      })
   }

   fun <K, V> bindMapValues(target: ObservableList<out V>, map: ObservableMap<out K, out V>) {
      Bindings.bindContent(target, observableMapValues(map))
   }
}