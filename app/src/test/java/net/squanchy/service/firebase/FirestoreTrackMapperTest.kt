package net.squanchy.service.firebase

import com.google.common.truth.Truth.assertThat
import net.squanchy.support.checksum.Checksum
import net.squanchy.support.lang.getOrThrow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

private const val AN_ID = "ABC123"
private const val A_NAME = "ABCD"
private const val A_COLOR = "#AB0123"

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
        val firestoreTrack = aFirestoreTrack(id = AN_ID)
        val track = firestoreTrack.toTrack(checksum)
        assertThat(track.id).isEqualTo(AN_ID)
    }

    @Test
    fun `track name should match when mapped`() {
        val firestoreTrack = aFirestoreTrack(name = A_NAME)
        val track = firestoreTrack.toTrack(checksum)
        assertThat(track.name).isEqualTo(A_NAME)
    }

    @Test
    fun `track url should match when mapped`() {
        val firestoreTrack = aFirestoreTrack(iconUrl = A_NAME)
        val track = firestoreTrack.toTrack(checksum)
        assertThat(track.iconUrl.getOrThrow()).isEqualTo(A_NAME)
    }

    @Test
    fun `track url should be empty when mapping null`() {
        val firestoreTrack = aFirestoreTrack(iconUrl = null)
        val track = firestoreTrack.toTrack(checksum)
        assertThat(track.iconUrl.isEmpty()).isTrue()
    }

    @Test
    fun `accent color should match when mapped`() {
        val firestoreTrack = aFirestoreTrack(accentColor = A_COLOR)
        val track = firestoreTrack.toTrack(checksum)
        assertThat(track.accentColor.getOrThrow()).isEqualTo(A_COLOR)
    }

    @Test
    fun `accent color should be empty when mapping null`() {
        val firestoreTrack = aFirestoreTrack(accentColor = null)
        val track = firestoreTrack.toTrack(checksum)
        assertThat(track.accentColor.isEmpty()).isTrue()
    }

    @Test
    fun `text color should match when mapped`() {
        val firestoreTrack = aFirestoreTrack(textColor = A_COLOR)
        val track = firestoreTrack.toTrack(checksum)
        assertThat(track.textColor.getOrThrow()).isEqualTo(A_COLOR)
    }

    @Test
    fun `text color should be empty when mapping null`() {
        val firestoreTrack = aFirestoreTrack(textColor = null)
        val track = firestoreTrack.toTrack(checksum)
        assertThat(track.textColor.isEmpty()).isTrue()
    }
}
