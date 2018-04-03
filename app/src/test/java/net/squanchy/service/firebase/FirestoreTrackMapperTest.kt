package net.squanchy.service.firebase

import net.squanchy.support.lang.Checksum
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

private const val FAKE_ID = "ABC123"
private const val FAKE_NAME = "ABCD"
private const val FAKE_COLOR = "#ABCDEF"

private const val A_VALUE = "whatever"
private const val A_CHECKSUM = 1000L

class FirestoreTrackMapperTest {

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var checksum: Checksum

    @Before
    fun before() {
        Mockito.`when`(checksum.getChecksumOf(A_VALUE)).thenReturn(A_CHECKSUM)
    }

    @Test
    fun `track id should match when mapped`() {
        val firestoreTrack = aFirestoreTrack(id = FAKE_ID)
        val track = firestoreTrack.toTrack(checksum)
        Assert.assertEquals(FAKE_ID, track.id)
    }

    @Test
    fun `track name should match when mapped`() {
        val firestoreTrack = aFirestoreTrack(name = FAKE_NAME)
        val track = firestoreTrack.toTrack(checksum)
        Assert.assertEquals(FAKE_NAME, track.name)
    }

    @Test
    fun `track url should match when mapped`() {
        val firestoreTrack = aFirestoreTrack(iconUrl = FAKE_NAME)
        val track = firestoreTrack.toTrack(checksum)
        Assert.assertEquals(FAKE_NAME, track.iconUrl.get())
    }

    @Test
    fun `track url should be absent when mapping null`() {
        val firestoreTrack = aFirestoreTrack(iconUrl = null)
        val track = firestoreTrack.toTrack(checksum)
        Assert.assertFalse(track.iconUrl.isPresent)
    }

    @Test
    fun `accent color should match when mapped`() {
        val firestoreTrack = aFirestoreTrack(accentColor = FAKE_COLOR)
        val track = firestoreTrack.toTrack(checksum)
        Assert.assertEquals(FAKE_COLOR, track.accentColor.get())
    }

    @Test
    fun `accent color should be absent when mapping null`() {
        val firestoreTrack = aFirestoreTrack(accentColor = null)
        val track = firestoreTrack.toTrack(checksum)
        Assert.assertFalse(track.accentColor.isPresent)
    }

    @Test
    fun `text color should match when mapped`() {
        val firestoreTrack = aFirestoreTrack(textColor = FAKE_COLOR)
        val track = firestoreTrack.toTrack(checksum)
        Assert.assertEquals(FAKE_COLOR, track.textColor.get())
    }

    @Test
    fun `text color should be absent when mapping null`() {
        val firestoreTrack = aFirestoreTrack(textColor = null)
        val track = firestoreTrack.toTrack(checksum)
        Assert.assertFalse(track.textColor.isPresent)
    }
}
