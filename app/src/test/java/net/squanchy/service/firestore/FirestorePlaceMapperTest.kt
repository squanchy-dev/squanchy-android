package net.squanchy.service.firestore

import org.junit.Assert
import org.junit.Test

private const val FAKE_ID = "ABC"
private const val FAKE_NAME = "ABCD"
private const val FAKE_FLOOR = "Floor1"

class FirestorePlaceMapperTest {

    @Test
    fun `place id should match when mapped`() {
        val firestorePlace = aFirestorePlace(id = FAKE_ID)
        val place = firestorePlace.toPlace()
        Assert.assertEquals(FAKE_ID, place.id)
    }

    @Test
    fun `place name should match when mapped`() {
        val firestorePlace = aFirestorePlace(name = FAKE_NAME)
        val place = firestorePlace.toPlace()
        Assert.assertEquals(FAKE_NAME, place.name)
    }

    @Test
    fun `place floor should match when mapped`() {
        val firestorePlace = aFirestorePlace(floor = FAKE_FLOOR)
        val place = firestorePlace.toPlace()
        Assert.assertEquals(FAKE_FLOOR, place.floor.get())
    }

    @Test
    fun `place floor should be absent when mapping null`() {
        val firestorePlace = aFirestorePlace(floor = null)
        val place = firestorePlace.toPlace()
        Assert.assertFalse(place.floor.isPresent)
    }
}
