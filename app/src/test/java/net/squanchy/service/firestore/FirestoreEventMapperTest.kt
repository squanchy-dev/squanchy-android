package net.squanchy.service.firestore

import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.schedule.domain.view.Event
import net.squanchy.support.lang.Checksum
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness
import java.util.Date

private const val FAKE_STRING = "eventId"
private const val FAKE_TYPE = "social"
private const val FAKE_EXPERIENCE_LEVEL = "intermediate"
private const val CHECKSUM_RESULT = 1000L

class FirestoreEventMapperTest {

    @Rule
    @JvmField
    var rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var checksum: Checksum

    lateinit var timeZone: DateTimeZone

    @Before
    fun before() {
        `when`(checksum.getChecksumOf(FAKE_STRING)).thenReturn(CHECKSUM_RESULT)
        timeZone = DateTimeZone.UTC
    }

    @Test
    fun `event id should match when mapped`() {
        val firestoreEvent = aFirestoreEvent(id = FAKE_STRING)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(FAKE_STRING, event.id)
    }

    @Test
    fun `event start date should match when mapped`() {
        val date = Date(1518471471)
        val firestoreEvent = aFirestoreEvent(startTime = date)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(LocalDateTime(date), event.startTime)
    }

    @Test
    fun `event end date should match when mapped`() {
        val date = Date(1518471471)
        val firestoreEvent = aFirestoreEvent(endTime = date)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(LocalDateTime(date), event.endTime)
    }

    @Test
    fun `event title should match when mapped`() {
        val firestoreEvent = aFirestoreEvent(title = FAKE_STRING)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(FAKE_STRING, event.title)
    }

    @Test
    fun `event place should match when mapped`() {
        val firestorePlace = aFirestorePlace()
        val firestoreEvent = aFirestoreEvent(place = firestorePlace)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(firestorePlace.toPlace(), event.place.get())
    }

    @Test
    fun `event place should be absent when mapping null`() {
        val firestoreEvent = aFirestoreEvent(place = null)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertFalse(event.place.isPresent)
    }

    @Test
    fun `event track should match when mapped`() {
        val firestoreTrack = aFirestoreTrack()
        val firestoreEvent = aFirestoreEvent(track = firestoreTrack)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(firestoreTrack.toTrack(), event.track.get())
    }

    @Test
    fun `event track should be absent when mapping null`() {
        val firestoreEvent = aFirestoreEvent(track = null)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertFalse(event.track.isPresent)
    }

    @Test
    fun `event experience level should match when mapped`() {
        val firestoreEvent = aFirestoreEvent(experienceLevel = FAKE_EXPERIENCE_LEVEL)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(ExperienceLevel.tryParsingFrom(FAKE_EXPERIENCE_LEVEL), event.experienceLevel)
    }

    @Test
    fun `event experience level should be absent when mapping null`() {
        val firestoreEvent = aFirestoreEvent(experienceLevel = null)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertFalse(event.experienceLevel.isPresent)
    }

    @Test
    fun `event timezone should match the parameter when mapped`() {
        val firestoreEvent = aFirestoreEvent()
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(event.timeZone, timeZone)
    }

    @Test
    fun `event description should match when mapped`() {
        val firestoreEvent = aFirestoreEvent(description = FAKE_STRING)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(FAKE_STRING, event.description.get())
    }

    @Test
    fun `event description should be absent when mapping null`() {
        val firestoreEvent = aFirestoreEvent(description = null)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertFalse(event.description.isPresent)
    }

    @Test
    fun `event type should match when mapped`() {
        val firestoreEvent = aFirestoreEvent(type = FAKE_TYPE)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(Event.Type.fromRawType(FAKE_TYPE), event.type)
    }
}
