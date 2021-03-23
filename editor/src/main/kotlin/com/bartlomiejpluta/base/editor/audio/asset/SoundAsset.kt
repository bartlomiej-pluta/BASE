package com.bartlomiejpluta.base.editor.audio.asset

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.project.model.Project

class SoundAsset(project: Project, uid: String, source: String, name: String) :
   Asset(project.audioDirectoryProperty, uid, source, name)