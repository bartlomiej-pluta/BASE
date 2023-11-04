package com.bartlomiejpluta.base.editor.code.build.asset

import com.bartlomiejpluta.base.editor.project.model.Project

interface AssetSerializer {
   fun serializeAssets(project: Project)
}