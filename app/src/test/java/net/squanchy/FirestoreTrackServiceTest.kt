package net.squanchy

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.firestore.model.schedule.FirestoreTrackService
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.model.schedule.FirestoreTrack
import net.squanchy.support.lang.Optional
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class FirestoreTrackServiceTest {

    @Rule
    @JvmField
    var rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var firestoreDbService: FirestoreDbService

    @Test
    fun `should emit an empty list of tracks when there are no tracks on the DB`() {
        val trackService = FirestoreTrackService(firestoreDbService)
        val subscription = TestObserver<List<Track>>()
        `when`(firestoreDbService.tracks()).thenReturn(Observable.just(emptyList()))

        trackService.tracks()
            .subscribe(subscription)

        subscription.assertValue(emptyList())
    }

    @Test
    fun `should map nulls in the DB events it receives to absent()s in the domain events it emits`() {
        val trackService = FirestoreTrackService(firestoreDbService)
        val subscription = TestObserver<List<Track>>()
        val firestoreTracks = listOf(FirestoreTrack().apply {
            id = "any"
            name = "name"
            accentColor = null
            iconUrl = null
            textColor = null
        })
        `when`(firestoreDbService.tracks()).thenReturn(Observable.just(firestoreTracks))

        trackService.tracks()
            .subscribe(subscription)

        val tracks = listOf(Track(
            id = "any",
            name = "name",
            accentColor = Optional.absent(),
            iconUrl = Optional.absent(),
            textColor = Optional.absent()
        ))
        subscription.assertValue(tracks)
    }

    @Test
    fun `should map all values in the DB events it receives to optionals in the domain events it emits`() {
        val trackService = FirestoreTrackService(firestoreDbService)
        val subscription = TestObserver<List<Track>>()
        val firestoreTracks = listOf(FirestoreTrack().apply {
            id = "any"
            name = "name"
            accentColor = "#ffff00"
            iconUrl = "http://example.com/icon.png"
            textColor = "#0000ff"
        })
        `when`(firestoreDbService.tracks()).thenReturn(Observable.just(firestoreTracks))

        trackService.tracks()
            .subscribe(subscription)

        val tracks = listOf(Track(
            id = "any",
            name = "name",
            accentColor = Optional.of("#ffff00"),
            iconUrl = Optional.of("http://example.com/icon.png"),
            textColor = Optional.of("#0000ff")
        ))
        subscription.assertValue(tracks)
    }
}
