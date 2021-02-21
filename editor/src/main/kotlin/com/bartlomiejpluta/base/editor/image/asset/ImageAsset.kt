package com.bartlomiejpluta.base.editor.image.asset

import com.bartlomiejpluta.base.editor.asset.model.GraphicAsset
import com.bartlomiejpluta.base.editor.project.model.Project

class ImageAsset(project: Project, uid: String, source: String, name: String) :
   GraphicAsset(project.imagesDirectoryProperty, uid, source, name)