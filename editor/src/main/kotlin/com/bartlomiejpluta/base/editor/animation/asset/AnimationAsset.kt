package com.bartlomiejpluta.base.editor.animation.asset

import com.bartlomiejpluta.base.editor.asset.model.GraphicAsset
import com.bartlomiejpluta.base.editor.project.model.Project

class AnimationAsset(project: Project, uid: String, source: String, name: String, rows: Int, columns: Int) :
   GraphicAsset(project.animationsDirectoryProperty, uid, source, name, rows, columns)