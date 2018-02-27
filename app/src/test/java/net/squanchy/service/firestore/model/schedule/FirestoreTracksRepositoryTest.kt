package net.squanchy.service.firestore.model.schedule

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import net.squanchy.schedule.domain.view.Track
import net.squanchy.schedule.domain.view.aTrack
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.aFirestoreTrack
import net.squanchy.service.repository.firestore.FirestoreTracksRepository
import net.squanchy.support.lang.Optional
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class FirestoreTracksRepositoryTest {

    @Rule
    @JvmField
    var rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var firestoreDbService: FirestoreDbService

    @Test
    fun `should emit an empty list of tracks when there are no tracks on the DB`() {
        val trackService = FirestoreTracksRepository(firestoreDbService)
        val subscription = TestObserver<List<Track>>()
        `when`(firestoreDbService.tracks()).thenReturn(Observable.just(emptyList()))

        trackService.tracks()
            .subscribe(subscription)

        subscription.assertValue(emptyList())
    }

    @Test
    fun `should map nulls in the DB events it receives to absent()s in the domain events it emits`() {
        val trackService = FirestoreTracksRepository(firestoreDbService)
        val subscription = TestObserver<List<Track>>()
        val firestoreTracks = listOf(aFirestoreTrack(accentColor = null, iconUrl = null, textColor = null))
        `when`(firestoreDbService.tracks()).thenReturn(Observable.just(firestoreTracks))

        trackService.tracks()
            .subscribe(subscription)

        val tracks = listOf(aTrack(
            accentColor = Optional.absent(),
            iconUrl = Optional.absent(),
            textColor = Optional.absent()
        ))
        subscription.assertValue(tracks)
    }

    @Test
    fun `should map all values in the DB events it receives to optionals in the domain events it emits`() {
        val trackService = FirestoreTracksRepository(firestoreDbService)
        val subscription = TestObserver<List<Track>>()
        val firestoreTracks = listOf(aFirestoreTrack())
        `when`(firestoreDbService.tracks()).thenReturn(Observable.just(firestoreTracks))

        trackService.tracks()
            .subscribe(subscription)

        val tracks = listOf(aTrack())
        subscription.assertValue(tracks)
    }
}
