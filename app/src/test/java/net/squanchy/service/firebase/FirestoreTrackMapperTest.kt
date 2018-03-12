package net.squanchy.service.firebase

import org.junit.Assert
import org.junit.Test

private const val FAKE_ID = "ABC123"
private const val FAKE_NAME = "ABCD"
private const val FAKE_COLOR = "#ABCDEF"

class FirestoreTrackMapperTest {

    @Test
    fun `track id should match when mapped`() {
        val firestoreTrack = aFirestoreTrack(id = FAKE_ID)
        val track = firestoreTrack.toTrack()
        Assert.assertEquals(FAKE_ID, track.id)
    }

    @Test
    fun `track name should match when mapped`() {
        val firestoreTrack = aFirestoreTrack(name = FAKE_NAME)
        val track = firestoreTrack.toTrack()
        Assert.assertEquals(FAKE_NAME, track.name)
    }

    @Test
    fun `track url should match when mapped`() {
        val firestoreTrack = aFirestoreTrack(iconUrl = FAKE_NAME)
        val track = firestoreTrack.toTrack()
        Assert.assertEquals(FAKE_NAME, track.iconUrl.get())
    }

    @Test
    fun `track url should be absent when mapping null`() {
        val firestoreTrack = aFirestoreTrack(iconUrl = null)
        val track = firestoreTrack.toTrack()
        Assert.assertFalse(track.iconUrl.isPresent)
    }

    @Test
    fun `accent color should match when mapped`() {
        val firestoreTrack = aFirestoreTrack(accentColor = FAKE_COLOR)
        val track = firestoreTrack.toTrack()
        Assert.assertEquals(FAKE_COLOR, track.accentColor.get())
    }

    @Test
    fun `accent color should be absent when mapping null`() {
        val firestoreTrack = aFirestoreTrack(accentColor = null)
        val track = firestoreTrack.toTrack()
        Assert.assertFalse(track.accentColor.isPresent)
    }

    @Test
    fun `text color should match when mapped`() {
        val firestoreTrack = aFirestoreTrack(textColor = FAKE_COLOR)
        val track = firestoreTrack.toTrack()
        Assert.assertEquals(FAKE_COLOR, track.textColor.get())
    }

    @Test
    fun `text color should be absent when mapping null`() {
        val firestoreTrack = aFirestoreTrack(textColor = null)
        val track = firestoreTrack.toTrack()
        Assert.assertFalse(track.textColor.isPresent)
    }
}
