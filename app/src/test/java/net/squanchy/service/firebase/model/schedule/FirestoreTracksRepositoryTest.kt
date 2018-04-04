package net.squanchy.service.firebase.model.schedule

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import net.squanchy.schedule.domain.view.Track
import net.squanchy.schedule.domain.view.aTrack
import net.squanchy.service.firebase.FirestoreDbService
import net.squanchy.service.firebase.aFirestoreTrack
import net.squanchy.service.repository.firestore.FirestoreTracksRepository
import net.squanchy.support.lang.Checksum
import net.squanchy.support.lang.Optional
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

private const val FAKE_TRACK_ID = "trackId"
private const val TRACK_ID_CHECKSUM = 0L

class FirestoreTracksRepositoryTest {

    @Rule
    @JvmField
    var rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var firestoreDbService: FirestoreDbService

    @Mock
    private lateinit var checksum: Checksum

    @Before
    fun before() {
        `when`(checksum.getChecksumOf(FAKE_TRACK_ID)).thenReturn(TRACK_ID_CHECKSUM)
    }

    @Test
    fun `should emit an empty list of tracks when there are no tracks on the DB`() {
        val trackService = FirestoreTracksRepository(firestoreDbService, checksum)
        val subscription = TestObserver<List<Track>>()
        `when`(firestoreDbService.tracks()).thenReturn(Observable.just(emptyList()))

        trackService.tracks()
            .subscribe(subscription)

        subscription.assertValue(emptyList())
    }

    @Test
    fun `should map nulls in the DB events it receives to absent()s in the domain events it emits`() {
        val trackService = FirestoreTracksRepository(firestoreDbService, checksum)
        val subscription = TestObserver<List<Track>>()
        val firestoreTracks = listOf(aFirestoreTrack(accentColor = null, iconUrl = null, textColor = null))
        `when`(firestoreDbService.tracks()).thenReturn(Observable.just(firestoreTracks))

        trackService.tracks()
            .subscribe(subscription)

        val tracks = listOf(
            aTrack(
                accentColor = Optional.absent(),
                iconUrl = Optional.absent(),
                textColor = Optional.absent()
            )
        )
        subscription.assertValue(tracks)
    }

    @Test
    fun `should map all values in the DB events it receives to optionals in the domain events it emits`() {
        val trackService = FirestoreTracksRepository(firestoreDbService, checksum)
        val subscription = TestObserver<List<Track>>()
        val firestoreTracks = listOf(aFirestoreTrack())
        `when`(firestoreDbService.tracks()).thenReturn(Observable.just(firestoreTracks))

        trackService.tracks()
            .subscribe(subscription)

        val tracks = listOf(aTrack())
        subscription.assertValue(tracks)
    }
}
