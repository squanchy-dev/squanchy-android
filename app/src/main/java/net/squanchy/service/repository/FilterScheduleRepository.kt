package net.squanchy.service.repository

import net.squanchy.schedule.domain.view.Track

interface FilterScheduleRepository {

    var filters: Set<Track>
    var allTracks: Set<Track>

    fun hasFilter(): Boolean
}

class LocalFilterScheduleRepository : FilterScheduleRepository {

    override var allTracks: Set<Track> = emptySet()
    override var filters: Set<Track> = emptySet()

    override fun hasFilter() = filters.isNotEmpty()
}
