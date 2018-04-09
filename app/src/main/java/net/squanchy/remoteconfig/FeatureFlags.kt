package net.squanchy.remoteconfig

class FeatureFlags(private val remoteConfig: RemoteConfig) {

    val showEventRoomInSchedule
        get() = isFeatureEnabled(Feature.ShowEventRoomInSchedule)

    private fun isFeatureEnabled(feature: Feature) = remoteConfig.getBoolean(feature.remoteConfigFlagName)

    private sealed class Feature {
        abstract val remoteConfigFlagName: String

        object ShowEventRoomInSchedule : Feature() {

            override val remoteConfigFlagName = "show_event_room_in_schedule"
        }
    }
}
