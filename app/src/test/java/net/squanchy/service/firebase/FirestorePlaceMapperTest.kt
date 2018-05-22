package net.squanchy.service.firebase

import com.google.common.truth.Truth.assertThat
import org.junit.Test

private const val FAKE_ID = "ABC"
private const val FAKE_NAME = "ABCD"
private const val FAKE_FLOOR = "Floor1"

class FirestorePlaceMapperTest {

    @Test
    fun `place id should match when mapped`() {
        val firestorePlace = aFirestorePlace(id = FAKE_ID)
        val place = firestorePlace.toPlace()
        assertThat(place.id).isEqualTo(FAKE_ID)
    }

    @Test
    fun `place name should match when mapped`() {
        val firestorePlace = aFirestorePlace(name = FAKE_NAME)
        val place = firestorePlace.toPlace()
        assertThat(place.name).isEqualTo(FAKE_NAME)
    }

    @Test
    fun `place floor should match when mapped`() {
        val firestorePlace = aFirestorePlace(floor = FAKE_FLOOR)
        val place = firestorePlace.toPlace()
        assertThat(place.floor.getOrThrow()).isEqualTo(FAKE_FLOOR)
    }

    @Test
    fun `place floor should be empty when mapping null`() {
        val firestorePlace = aFirestorePlace(floor = null)
        val place = firestorePlace.toPlace()
        assertThat(place.floor.isEmpty()).isTrue()
    }
}
