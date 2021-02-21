package com.bartlomiejpluta.base.editor.map.asset

import com.bartlomiejpluta.base.editor.asset.model.GraphicAsset
import com.bartlomiejpluta.base.editor.project.model.Project

class GameMapAsset(project: Project, uid: String, name: String) :
   GraphicAsset(project.mapsDirectoryProperty, project.mapsDirectoryProperty, uid, "$uid.dat", "$uid.png", name)