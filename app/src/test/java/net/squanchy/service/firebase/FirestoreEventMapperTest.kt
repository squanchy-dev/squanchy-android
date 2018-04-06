package net.squanchy.service.firebase

import net.squanchy.A_DATE
import net.squanchy.A_TIMEZONE
import com.google.common.truth.Truth.assertThat
import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.schedule.domain.view.Event
import net.squanchy.support.checksum.Checksum
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.util.Date

private const val A_VALUE = "whatever"
private const val A_CHECKSUM = 1000L

private const val FAKE_TYPE = "social"
private const val FAKE_EXPERIENCE_LEVEL = "intermediate"

class FirestoreEventMapperTest {

    @Rule
    @JvmField
    var rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var checksum: Checksum

    @Before
    fun before() {
        `when`(checksum.getChecksumOf(A_VALUE)).thenReturn(A_CHECKSUM)
    }

    @Test
    fun `event id should match when mapped`() {
        val firestoreEvent = aFirestoreEvent(id = A_VALUE)
        val event = firestoreEvent.toEvent(checksum, A_TIMEZONE)
        assertThat(event.id).isEqualTo(A_VALUE)
    }

    @Test
    fun `event start date should match when mapped`() {
        val date = Date(1518471471)
        val firestoreEvent = aFirestoreEvent(startTime = date)
        val event = firestoreEvent.toEvent(checksum, A_TIMEZONE)
        assertThat(event.startTime).isEqualTo(A_DATE.toDateTime(A_TIMEZONE).toLocalDateTime())
    }

    @Test
    fun `event end date should match when mapped`() {
        val date = Date(1518471471)
        val firestoreEvent = aFirestoreEvent(endTime = date)
        val event = firestoreEvent.toEvent(checksum, A_TIMEZONE)
        assertThat(event.endTime).isEqualTo(A_DATE.toDateTime(A_TIMEZONE).toLocalDateTime())
    }

    @Test
    fun `event title should match when mapped`() {
        val firestoreEvent = aFirestoreEvent(title = A_VALUE)
        val event = firestoreEvent.toEvent(checksum, A_TIMEZONE)
        assertThat(event.title).isEqualTo(A_VALUE)
    }

    @Test
    fun `event place should match when mapped`() {
        val firestorePlace = aFirestorePlace()
        val firestoreEvent = aFirestoreEvent(place = firestorePlace)
        val event = firestoreEvent.toEvent(checksum, A_TIMEZONE)
        assertThat(firestorePlace.toPlace()).isEqualTo(event.place.get())
    }

    @Test
    fun `event place should be absent when mapping null`() {
        val firestoreEvent = aFirestoreEvent(place = null)
        val event = firestoreEvent.toEvent(checksum, A_TIMEZONE)
        assertThat(event.place.isPresent).isFalse()
    }

    @Test
    fun `event track should match when mapped`() {
        val firestoreTrack = aFirestoreTrack()
        val firestoreEvent = aFirestoreEvent(track = firestoreTrack)
        val event = firestoreEvent.toEvent(checksum, A_TIMEZONE)
        assertThat(firestoreTrack.toTrack(checksum)).isEqualTo(event.track.get())
    }

    @Test
    fun `event track should be absent when mapping null`() {
        val firestoreEvent = aFirestoreEvent(track = null)
        val event = firestoreEvent.toEvent(checksum, A_TIMEZONE)
        assertThat(event.track.isPresent).isFalse()
    }

    @Test
    fun `event experience level should match when mapped`() {
        val firestoreEvent = aFirestoreEvent(experienceLevel = FAKE_EXPERIENCE_LEVEL)
        val event = firestoreEvent.toEvent(checksum, A_TIMEZONE)
        assertThat(ExperienceLevel.tryParsingFrom(FAKE_EXPERIENCE_LEVEL)).isEqualTo(event.experienceLevel)
    }

    @Test
    fun `event experience level should be absent when mapping null`() {
        val firestoreEvent = aFirestoreEvent(experienceLevel = null)
        val event = firestoreEvent.toEvent(checksum, A_TIMEZONE)
        assertThat(event.experienceLevel.isPresent).isFalse()
    }

    @Test
    fun `event timezone should match the parameter when mapped`() {
        val firestoreEvent = aFirestoreEvent()
        val event = firestoreEvent.toEvent(checksum, A_TIMEZONE)
        assertThat(event.timeZone).isEqualTo(A_TIMEZONE)
    }

    @Test
    fun `event description should match when mapped`() {
        val firestoreEvent = aFirestoreEvent(description = A_VALUE)
        val event = firestoreEvent.toEvent(checksum, A_TIMEZONE)
        assertThat(A_VALUE).isEqualTo(event.description.get())
    }

    @Test
    fun `event description should be absent when mapping null`() {
        val firestoreEvent = aFirestoreEvent(description = null)
        val event = firestoreEvent.toEvent(checksum, A_TIMEZONE)
        assertThat(event.description.isPresent).isFalse()
    }

    @Test
    fun `event type should match when mapped`() {
        val firestoreEvent = aFirestoreEvent(type = FAKE_TYPE)
        val event = firestoreEvent.toEvent(checksum, A_TIMEZONE)
        assertThat(Event.Type.fromRawType(FAKE_TYPE)).isEqualTo(event.type)
    }

    @Test
    fun `place floor should match when mapped`() {
        val firestorePlace = aFirestorePlace()
        val place = firestorePlace.toPlace()
        assertThat(place.floor.get()).isEqualTo(firestorePlace.floor)
    }

    @Test
    fun `place floor should be absent when mapping null`() {
        val firestorePlace = aFirestorePlace(floor = null)
        val place = firestorePlace.toPlace()
        assertThat(place.floor.isPresent).isFalse()
    }

    @Test
    fun `place id should match when mapped`() {
        val firestorePlace = aFirestorePlace()
        val place = firestorePlace.toPlace()
        assertThat(place.id).isEqualTo(firestorePlace.id)
    }

    @Test
    fun `place name should match when mapped`() {
        val firestorePlace = aFirestorePlace()
        val place = firestorePlace.toPlace()
        assertThat(place.name).isEqualTo(firestorePlace.name)
    }
}
