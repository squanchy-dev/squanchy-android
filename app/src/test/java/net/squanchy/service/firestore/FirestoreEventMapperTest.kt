package net.squanchy.service.firestore

import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.schedule.domain.view.Event
import net.squanchy.support.lang.Checksum
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.Date

private const val FAKE_STRING = "eventId"
private const val FAKE_TYPE = "social"
private const val FAKE_EXPERIENCE_LEVEL = "intermediate"
private const val CHECKSUM_RESULT = 1000L

class FirestoreEventMapperTest {

    @Mock
    lateinit var checksum: Checksum

    lateinit var timeZone: DateTimeZone

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        `when`(checksum.getChecksumOf(FAKE_STRING)).thenReturn(CHECKSUM_RESULT)
        timeZone = DateTimeZone.UTC
    }

    @Test
    fun `event id should match after mapping`() {
        val firestoreEvent = fixtureOfFirestoreEvent(id = FAKE_STRING)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(FAKE_STRING, event.id)
    }

    @Test
    fun `event start date should match after mapping`() {
        val date = Date(1518471471)
        val firestoreEvent = fixtureOfFirestoreEvent(startTime = date)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(LocalDateTime(date), event.startTime)
    }

    @Test
    fun `event end date should match after mapping`() {
        val date = Date(1518471471)
        val firestoreEvent = fixtureOfFirestoreEvent(endTime = date)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(LocalDateTime(date), event.endTime)
    }

    @Test
    fun `event title should match after mapping`() {
        val firestoreEvent = fixtureOfFirestoreEvent(title = FAKE_STRING)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(FAKE_STRING, event.title)
    }

    @Test
    fun `event place should match after mapping`() {
        val firestorePlace = fixtureOfFirestorePlace()
        val firestoreEvent = fixtureOfFirestoreEvent(place = firestorePlace)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(firestorePlace.toPlace(), event.place.get())
    }

    @Test
    fun `event place should be absent when is null`() {
        val firestoreEvent = fixtureOfFirestoreEvent(place = null)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertFalse(event.place.isPresent)
    }

    @Test
    fun `event track should match after mapping`() {
        val firestoreTrack = fixtureOfFirestoreTrack()
        val firestoreEvent = fixtureOfFirestoreEvent(track = firestoreTrack)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(firestoreTrack.toTrack(), event.track.get())
    }

    @Test
    fun `event track should be absent when is null`() {
        val firestoreEvent = fixtureOfFirestoreEvent(track = null)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertFalse(event.track.isPresent)
    }

    @Test
    fun `event experience level should match after mapping`() {
        val firestoreEvent = fixtureOfFirestoreEvent(experienceLevel = FAKE_EXPERIENCE_LEVEL)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(ExperienceLevel.tryParsingFrom(FAKE_EXPERIENCE_LEVEL), event.experienceLevel)
    }

    @Test
    fun `event experience level should be absent when is null`() {
        val firestoreEvent = fixtureOfFirestoreEvent(experienceLevel = null)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertFalse(event.experienceLevel.isPresent)
    }

    @Test
    fun `event timezone should match the parameter`() {
        val firestoreEvent = fixtureOfFirestoreEvent()
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(event.timeZone, timeZone)
    }

    @Test
    fun `event description should match after mapping`() {
        val firestoreEvent = fixtureOfFirestoreEvent(description = FAKE_STRING)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(FAKE_STRING, event.description.get())
    }

    @Test
    fun `event description should be absent when is null`() {
        val firestoreEvent = fixtureOfFirestoreEvent(description = null)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertFalse(event.description.isPresent)
    }

    @Test
    fun `event type should match after mapping`() {
        val firestoreEvent = fixtureOfFirestoreEvent(type = FAKE_TYPE)
        val event = firestoreEvent.toEvent(checksum, timeZone)
        Assert.assertEquals(Event.Type.fromRawType(FAKE_TYPE), event.type)
    }
}
