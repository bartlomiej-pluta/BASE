package com.bartlomiejpluta.base.editor.map.serial

import com.bartlomiejpluta.base.editor.common.serial.Deserializer
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import java.io.InputStream

interface BinaryMapDeserializer : Deserializer<GameMap> {
   fun deserialize(input: InputStream, replaceTileSet: (String, String) -> String, replaceAutoTile: (String, String) -> String): GameMap
}