package com.bartlomiejpluta.base.editor.animation.asset

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.project.model.Project

class AnimationAsset(project: Project, uid: String, source: String, name: String, val rows: Int, val columns: Int) :
   Asset(project.animationsDirectoryProperty, uid, source, name)