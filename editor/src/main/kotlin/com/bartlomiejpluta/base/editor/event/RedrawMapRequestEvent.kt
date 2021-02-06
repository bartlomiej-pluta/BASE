package com.bartlomiejpluta.base.editor.event

import tornadofx.EventBus.RunOn.ApplicationThread
import tornadofx.EventBus.RunOn.BackgroundThread
import tornadofx.FXEvent

object RedrawMapRequestEvent : FXEvent(ApplicationThread)