package net.squanchy.service.firebase

import com.google.common.truth.Truth.assertThat
import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.schedule.domain.view.Event
import net.squanchy.support.checksum.Checksum
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
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

    private lateinit var timeZone: DateTimeZone

    @Before
    fun before() {
        `when`(checksum.getChecksumOf(A_VALUE)).thenReturn(A_CHECKSUM)
        timeZone = DateTimeZone.UTC
    }

    @Test
    fun `event id should match when mapped`() {
        val firestoreEvent = aFirestoreEvent(id = A_VALUE)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        assertThat(A_VALUE).isEqualTo(event.id)
    }

    @Test
    fun `event start date should match when mapped`() {
        val date = Date(1518471471)
        val firestoreEvent = aFirestoreEvent(startTime = date)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        assertThat(LocalDateTime(date)).isEqualTo(event.startTime)
    }

    @Test
    fun `event end date should match when mapped`() {
        val date = Date(1518471471)
        val firestoreEvent = aFirestoreEvent(endTime = date)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        assertThat(LocalDateTime(date)).isEqualTo(event.endTime)
    }

    @Test
    fun `event title should match when mapped`() {
        val firestoreEvent = aFirestoreEvent(title = A_VALUE)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        assertThat(A_VALUE).isEqualTo(event.title)
    }

    @Test
    fun `event place should match when mapped`() {
        val firestorePlace = aFirestorePlace()
        val firestoreEvent = aFirestoreEvent(place = firestorePlace)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        assertThat(firestorePlace.toPlace()).isEqualTo(event.place.get())
    }

    @Test
    fun `event place should be absent when mapping null`() {
        val firestoreEvent = aFirestoreEvent(place = null)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        assertThat(event.place.isPresent).isFalse()
    }

    @Test
    fun `event track should match when mapped`() {
        val firestoreTrack = aFirestoreTrack()
        val firestoreEvent = aFirestoreEvent(track = firestoreTrack)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        assertThat(firestoreTrack.toTrack(checksum)).isEqualTo(event.track.get())
    }

    @Test
    fun `event track should be absent when mapping null`() {
        val firestoreEvent = aFirestoreEvent(track = null)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        assertThat(event.track.isPresent).isFalse()
    }

    @Test
    fun `event experience level should match when mapped`() {
        val firestoreEvent = aFirestoreEvent(experienceLevel = FAKE_EXPERIENCE_LEVEL)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        assertThat(ExperienceLevel.tryParsingFrom(FAKE_EXPERIENCE_LEVEL)).isEqualTo(event.experienceLevel)
    }

    @Test
    fun `event experience level should be absent when mapping null`() {
        val firestoreEvent = aFirestoreEvent(experienceLevel = null)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        assertThat(event.experienceLevel.isPresent).isFalse()
    }

    @Test
    fun `event timezone should match the parameter when mapped`() {
        val firestoreEvent = aFirestoreEvent()
        val event = firestoreEvent.toEvent(checksum, timeZone)
        assertThat(event.timeZone).isEqualTo(timeZone)
    }

    @Test
    fun `event description should match when mapped`() {
        val firestoreEvent = aFirestoreEvent(description = A_VALUE)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        assertThat(A_VALUE).isEqualTo(event.description.get())
    }

    @Test
    fun `event description should be absent when mapping null`() {
        val firestoreEvent = aFirestoreEvent(description = null)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        assertThat(event.description.isPresent).isFalse()
    }

    @Test
    fun `event type should match when mapped`() {
        val firestoreEvent = aFirestoreEvent(type = FAKE_TYPE)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        assertThat(Event.Type.fromRawType(FAKE_TYPE)).isEqualTo(event.type)
    }
}
