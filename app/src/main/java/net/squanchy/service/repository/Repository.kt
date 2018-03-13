package net.squanchy.service.repository

import io.reactivex.Completable
import io.reactivex.Observable
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Track
import net.squanchy.speaker.domain.view.Speaker

interface EventRepository {

    fun event(eventId: String, userId: String): Observable<Event>

    fun events(userId: String): Observable<List<Event>>

    fun addFavorite(eventId: String, userId: String): Completable

    fun removeFavorite(eventId: String, userId: String): Completable
}

interface SpeakerRepository {

    fun speakers(): Observable<List<Speaker>>

    fun speaker(speakerId: String): Observable<Speaker>
}

interface TracksRepository {

    fun tracks(): Observable<List<Track>>
}
