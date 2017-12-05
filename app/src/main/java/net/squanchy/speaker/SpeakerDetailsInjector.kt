package net.squanchy.speaker

import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule

internal fun speakerDetailsComponent(activity: SpeakerDetailsActivity) =
    DaggerSpeakerDetailsComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .speakerDetailsModule(SpeakerDetailsModule())
        .activityContextModule(ActivityContextModule(activity))
        .navigationModule(NavigationModule())
        .build()
