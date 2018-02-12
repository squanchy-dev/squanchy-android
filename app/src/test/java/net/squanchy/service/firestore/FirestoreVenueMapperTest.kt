package net.squanchy.service.firestore

import com.google.firebase.firestore.GeoPoint
import org.junit.Assert
import org.junit.Test

private const val FAKE_STRING = "Fake Something"
private const val FAKE_COORDINATE = 5.87654

class FirestoreVenueMapperTest {

    @Test
    fun `venue name should match after mapping`() {
        val firestoreVenue = fixtureOfFirestoreVenue(name = FAKE_STRING)
        val venue = firestoreVenue.toVenue()
        Assert.assertEquals(FAKE_STRING, venue.name)
    }

    @Test
    fun `venue address should match after mapping`() {
        val firestoreVenue = fixtureOfFirestoreVenue(address = FAKE_STRING)
        val venue = firestoreVenue.toVenue()
        Assert.assertEquals(FAKE_STRING, venue.address)
    }

    @Test
    fun `venue description should match after mapping`() {
        val firestoreVenue = fixtureOfFirestoreVenue(description = FAKE_STRING)
        val venue = firestoreVenue.toVenue()
        Assert.assertEquals(FAKE_STRING, venue.description)
    }

    @Test
    fun `venue mapUrl should match after mapping`() {
        val firestoreVenue = fixtureOfFirestoreVenue(mapUrl = FAKE_STRING)
        val venue = firestoreVenue.toVenue()
        Assert.assertEquals(FAKE_STRING, venue.mapUrl)
    }

    @Test
    fun `venue lat should match after mapping`() {
        val firestoreVenue = fixtureOfFirestoreVenue(latLon = GeoPoint(FAKE_COORDINATE, 0.0))
        val venue = firestoreVenue.toVenue()
        Assert.assertEquals(FAKE_COORDINATE, venue.latitude, 0.1)
    }

    @Test
    fun `venue long should match after mapping`() {
        val firestoreVenue = fixtureOfFirestoreVenue(latLon = GeoPoint(0.0, FAKE_COORDINATE))
        val venue = firestoreVenue.toVenue()
        Assert.assertEquals(FAKE_COORDINATE, venue.longitude, 0.1)
    }
}
