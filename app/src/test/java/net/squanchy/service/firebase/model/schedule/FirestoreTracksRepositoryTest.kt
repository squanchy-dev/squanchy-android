package net.squanchy.service.firebase.model.schedule

import io.reactivex.Observable
import net.squanchy.schedule.domain.view.aTrack
import net.squanchy.schedule.firestore.FirestoreTracksRepository
import net.squanchy.schedule.firestore.aFirestoreTrack
import net.squanchy.service.firebase.FirestoreDbService
import net.squanchy.support.checksum.Checksum
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
        `when`(firestoreDbService.tracks()).thenReturn(Observable.just(emptyList()))

        trackService.tracks()
            .test()
            .assertValue(emptyList())
    }

    @Test
    fun `should map nulls in the DB events it receives to empty()s in the domain events it emits`() {
        val trackService = FirestoreTracksRepository(firestoreDbService, checksum)
        val firestoreTracks = listOf(aFirestoreTrack(accentColor = null, iconUrl = null, textColor = null))
        `when`(firestoreDbService.tracks()).thenReturn(Observable.just(firestoreTracks))

        trackService.tracks()
            .test()
            .assertValue(
                listOf(aTrack(accentColor = null, iconUrl = null, textColor = null))
            )
    }

    @Test
    fun `should map all values in the DB events it receives to optionals in the domain events it emits`() {
        val trackService = FirestoreTracksRepository(firestoreDbService, checksum)
        val firestoreTracks = listOf(aFirestoreTrack())
        `when`(firestoreDbService.tracks()).thenReturn(Observable.just(firestoreTracks))

        trackService.tracks()
            .test()
            .assertValue(listOf(aTrack()))
    }
}
