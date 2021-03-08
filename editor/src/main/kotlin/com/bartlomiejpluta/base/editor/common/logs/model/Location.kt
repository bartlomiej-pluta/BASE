package com.bartlomiejpluta.base.editor.common.logs.model

data class Location(val sourceName: String, val lineNumber: Long, val columnNumber: Long) {
   override fun toString() = if(lineNumber < 1) sourceName else "$sourceName:$lineNumber,$columnNumber"
}