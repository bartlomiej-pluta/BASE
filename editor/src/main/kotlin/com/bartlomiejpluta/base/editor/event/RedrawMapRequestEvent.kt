package com.bartlomiejpluta.base.editor.event

import tornadofx.EventBus.RunOn.BackgroundThread
import tornadofx.FXEvent

object RedrawMapRequestEvent : FXEvent(BackgroundThread)