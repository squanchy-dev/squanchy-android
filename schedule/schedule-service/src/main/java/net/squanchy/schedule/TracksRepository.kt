package net.squanchy.schedule

import io.reactivex.Observable
import net.squanchy.schedule.domain.view.Track

interface TracksRepository {

    fun tracks(): Observable<List<Track>>
}
