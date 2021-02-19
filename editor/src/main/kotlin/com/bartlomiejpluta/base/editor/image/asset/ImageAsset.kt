package com.bartlomiejpluta.base.editor.image.asset

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.project.model.Project

class ImageAsset(project: Project, uid: String, source: String, name: String) :
   Asset(project.imagesDirectoryProperty, uid, source, name)