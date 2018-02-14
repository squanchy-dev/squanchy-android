package net.squanchy.service.firestore

import net.squanchy.support.lang.Checksum
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness

private const val CHECKSUM_RESULT = 1000L
private const val FAKE_SPEAKER_STRING = "Mr William Shatner"

class FirestoreSpeakerMapperTest {

    @Rule
    var rule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @Mock
    lateinit var checksum: Checksum

    @Before
    fun before() {
        `when`(checksum.getChecksumOf(anyString())).thenReturn(CHECKSUM_RESULT)
    }

    @Test
    fun `verify numeric id is assigned through checksum`() {
        val firestoreSpeaker = fixtureOfFirestoreSpeaker()
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        Assert.assertEquals(CHECKSUM_RESULT, speaker.numericId)
    }

    @Test
    fun `speaker id should match when mapped`() {
        val firestoreSpeaker = fixtureOfFirestoreSpeaker(id = FAKE_SPEAKER_STRING)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        Assert.assertEquals(FAKE_SPEAKER_STRING, speaker.id)
    }

    @Test
    fun `speaker name should match when mapped`() {
        val firestoreSpeaker = fixtureOfFirestoreSpeaker(name = FAKE_SPEAKER_STRING)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        Assert.assertEquals(FAKE_SPEAKER_STRING, speaker.name)
    }

    @Test
    fun `speaker bio should match when mapped`() {
        val firestoreSpeaker = fixtureOfFirestoreSpeaker(bio = FAKE_SPEAKER_STRING)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        Assert.assertEquals(FAKE_SPEAKER_STRING, speaker.bio)
    }

    @Test
    fun `speaker company should match when mapped`() {
        val firestoreSpeaker = fixtureOfFirestoreSpeaker(companyName = FAKE_SPEAKER_STRING)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        Assert.assertEquals(FAKE_SPEAKER_STRING, speaker.companyName.get())
    }

    @Test
    fun `speaker company should be absent when mapping null`() {
        val firestoreSpeaker = fixtureOfFirestoreSpeaker(companyName = null)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        Assert.assertFalse(speaker.companyName.isPresent)
    }

    @Test
    fun `speaker company url should match when mapped`() {
        val firestoreSpeaker = fixtureOfFirestoreSpeaker(companyUrl = FAKE_SPEAKER_STRING)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        Assert.assertEquals(FAKE_SPEAKER_STRING, speaker.companyUrl.get())
    }

    @Test
    fun `speaker company url should be absent when mapping null`() {
        val firestoreSpeaker = fixtureOfFirestoreSpeaker(companyUrl = null)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        Assert.assertFalse(speaker.companyUrl.isPresent)
    }

    @Test
    fun `speaker url should match when mapped`() {
        val firestoreSpeaker = fixtureOfFirestoreSpeaker(personalUrl = FAKE_SPEAKER_STRING)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        Assert.assertEquals(FAKE_SPEAKER_STRING, speaker.personalUrl.get())
    }

    @Test
    fun `speaker url should be absent when mapping null`() {
        val firestoreSpeaker = fixtureOfFirestoreSpeaker(personalUrl = null)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        Assert.assertFalse(speaker.personalUrl.isPresent)
    }

    @Test
    fun `speaker twitter username should match when mapped`() {
        val firestoreSpeaker = fixtureOfFirestoreSpeaker(twitterUsername = FAKE_SPEAKER_STRING)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        Assert.assertEquals(FAKE_SPEAKER_STRING, speaker.twitterUsername.get())
    }

    @Test
    fun `speaker twitter username should be absent when mapping null`() {
        val firestoreSpeaker = fixtureOfFirestoreSpeaker(twitterUsername = null)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        Assert.assertFalse(speaker.twitterUsername.isPresent)
    }

    @Test
    fun `speaker photo url should match when mapped`() {
        val firestoreSpeaker = fixtureOfFirestoreSpeaker(personalUrl = FAKE_SPEAKER_STRING)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        Assert.assertEquals(FAKE_SPEAKER_STRING, speaker.personalUrl.get())
    }

    @Test
    fun `speaker photo url should be absent when mapping null`() {
        val firestoreSpeaker = fixtureOfFirestoreSpeaker(personalUrl = null)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        Assert.assertFalse(speaker.personalUrl.isPresent)
    }
}
