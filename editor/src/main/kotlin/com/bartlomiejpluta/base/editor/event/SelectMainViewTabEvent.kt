package com.bartlomiejpluta.base.editor.event

import tornadofx.EventBus
import tornadofx.FXEvent
import tornadofx.Scope

class SelectMainViewTabEvent(val targetScope: Scope) : FXEvent(EventBus.RunOn.ApplicationThread)