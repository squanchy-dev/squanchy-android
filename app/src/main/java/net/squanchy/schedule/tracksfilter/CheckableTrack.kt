package net.squanchy.schedule.tracksfilter

import net.squanchy.schedule.domain.view.Track

internal typealias CheckableTrack = Pair<Track, Boolean>

internal fun Iterable<CheckableTrack>.allSelected(): Set<Track> =
    filter { it.selected() }.map { it.track() }.toSet()

internal fun CheckableTrack.track() = this.first
internal fun CheckableTrack.selected() = this.second
