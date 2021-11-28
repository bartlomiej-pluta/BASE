package com.bartlomiejpluta.base.editor.map.serial

import com.bartlomiejpluta.base.editor.common.serial.Deserializer
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.tileset.model.TileSet
import java.io.InputStream

interface MapDeserializer : Deserializer<GameMap> {
   fun deserialize(input: InputStream, tileSet: TileSet): GameMap
}