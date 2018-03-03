package net.squanchy.service.repository

import io.reactivex.Observable
import net.squanchy.schedule.domain.view.Track

interface TracksRepository {

    fun tracks(): Observable<List<Track>>
}
