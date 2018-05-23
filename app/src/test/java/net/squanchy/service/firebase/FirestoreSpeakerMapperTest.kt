package net.squanchy.service.firebase

import com.google.common.truth.Truth.assertThat
import net.squanchy.schedule.firestore.toSpeaker
import net.squanchy.support.checksum.Checksum
import net.squanchy.support.lang.getOrThrow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

private const val CHECKSUM_RESULT = 1000L
private const val FAKE_SPEAKER_STRING = "Mr William Shatner"

class FirestoreSpeakerMapperTest {

    @Rule
    @JvmField
    var rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var checksum: Checksum

    @Before
    fun before() {
        `when`(checksum.getChecksumOf(anyString())).thenReturn(CHECKSUM_RESULT)
    }

    @Test
    fun `verify numeric id is assigned through checksum when mapped`() {
        val firestoreSpeaker = aFirestoreSpeaker()
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        assertThat(speaker.numericId).isEqualTo(CHECKSUM_RESULT)
    }

    @Test
    fun `speaker id should match when mapped`() {
        val firestoreSpeaker = aFirestoreSpeaker(id = FAKE_SPEAKER_STRING)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        assertThat(speaker.id).isEqualTo(FAKE_SPEAKER_STRING)
    }

    @Test
    fun `speaker name should match when mapped`() {
        val firestoreSpeaker = aFirestoreSpeaker(name = FAKE_SPEAKER_STRING)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        assertThat(speaker.name).isEqualTo(FAKE_SPEAKER_STRING)
    }

    @Test
    fun `speaker bio should match when mapped`() {
        val firestoreSpeaker = aFirestoreSpeaker(bio = FAKE_SPEAKER_STRING)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        assertThat(speaker.bio).isEqualTo(FAKE_SPEAKER_STRING)
    }

    @Test
    fun `speaker company should match when mapped`() {
        val firestoreSpeaker = aFirestoreSpeaker(companyName = FAKE_SPEAKER_STRING)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        assertThat(speaker.companyName.getOrThrow()).isEqualTo(FAKE_SPEAKER_STRING)
    }

    @Test
    fun `speaker company should be empty when mapping null`() {
        val firestoreSpeaker = aFirestoreSpeaker(companyName = null)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        assertThat(speaker.companyName.isEmpty()).isTrue()
    }

    @Test
    fun `speaker company url should match when mapped`() {
        val firestoreSpeaker = aFirestoreSpeaker(companyUrl = FAKE_SPEAKER_STRING)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        assertThat(speaker.companyUrl.getOrThrow()).isEqualTo(FAKE_SPEAKER_STRING)
    }

    @Test
    fun `speaker company url should be empty when mapping null`() {
        val firestoreSpeaker = aFirestoreSpeaker(companyUrl = null)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        assertThat(speaker.companyUrl.isEmpty()).isTrue()
    }

    @Test
    fun `speaker url should match when mapped`() {
        val firestoreSpeaker = aFirestoreSpeaker(personalUrl = FAKE_SPEAKER_STRING)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        assertThat(speaker.personalUrl.getOrThrow()).isEqualTo(FAKE_SPEAKER_STRING)
    }

    @Test
    fun `speaker url should be empty when mapping null`() {
        val firestoreSpeaker = aFirestoreSpeaker(personalUrl = null)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        assertThat(speaker.personalUrl.isEmpty()).isTrue()
    }

    @Test
    fun `speaker twitter username should match when mapped`() {
        val firestoreSpeaker = aFirestoreSpeaker(twitterUsername = FAKE_SPEAKER_STRING)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        assertThat(speaker.twitterUsername.getOrThrow()).isEqualTo(FAKE_SPEAKER_STRING)
    }

    @Test
    fun `speaker twitter username should be empty when mapping null`() {
        val firestoreSpeaker = aFirestoreSpeaker(twitterUsername = null)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        assertThat(speaker.twitterUsername.isEmpty()).isTrue()
    }

    @Test
    fun `speaker photo url should match when mapped`() {
        val firestoreSpeaker = aFirestoreSpeaker(personalUrl = FAKE_SPEAKER_STRING)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        assertThat(speaker.personalUrl.getOrThrow()).isEqualTo(FAKE_SPEAKER_STRING)
    }

    @Test
    fun `speaker photo url should be empty when mapping null`() {
        val firestoreSpeaker = aFirestoreSpeaker(personalUrl = null)
        val speaker = firestoreSpeaker.toSpeaker(checksum)
        assertThat(speaker.personalUrl.isEmpty()).isTrue()
    }
}
