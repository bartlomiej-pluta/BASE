package com.bartlomiejpluta.base.editor.map.asset

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.project.model.Project

class GameMapAsset(project: Project, uid: String, name: String) :
   Asset(project.mapsDirectoryProperty, uid, "$uid.dat", name)