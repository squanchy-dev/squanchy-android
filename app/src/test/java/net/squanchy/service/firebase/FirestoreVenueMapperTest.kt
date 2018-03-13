package net.squanchy.service.firebase

import com.google.firebase.firestore.GeoPoint
import org.junit.Assert
import org.junit.Test

private const val FAKE_STRING = "Fake Something"
private const val FAKE_COORDINATE = 5.87654

class FirestoreVenueMapperTest {

    @Test
    fun `venue name should match when mapped`() {
        val firestoreVenue = aFirestoreVenue(name = FAKE_STRING)
        val venue = firestoreVenue.toVenue()
        Assert.assertEquals(FAKE_STRING, venue.name)
    }

    @Test
    fun `venue address should match when mapped`() {
        val firestoreVenue = aFirestoreVenue(address = FAKE_STRING)
        val venue = firestoreVenue.toVenue()
        Assert.assertEquals(FAKE_STRING, venue.address)
    }

    @Test
    fun `venue description should match when mapped`() {
        val firestoreVenue = aFirestoreVenue(description = FAKE_STRING)
        val venue = firestoreVenue.toVenue()
        Assert.assertEquals(FAKE_STRING, venue.description)
    }

    @Test
    fun `venue mapUrl should match when mapped`() {
        val firestoreVenue = aFirestoreVenue(mapUrl = FAKE_STRING)
        val venue = firestoreVenue.toVenue()
        Assert.assertEquals(FAKE_STRING, venue.mapUrl)
    }

    @Test
    fun `venue lat should match when mapped`() {
        val firestoreVenue = aFirestoreVenue(latLon = GeoPoint(FAKE_COORDINATE, 0.0))
        val venue = firestoreVenue.toVenue()
        Assert.assertEquals(FAKE_COORDINATE, venue.latitude, 0.1)
    }

    @Test
    fun `venue long should match when mapped`() {
        val firestoreVenue = aFirestoreVenue(latLon = GeoPoint(0.0, FAKE_COORDINATE))
        val venue = firestoreVenue.toVenue()
        Assert.assertEquals(FAKE_COORDINATE, venue.longitude, 0.1)
    }
}
