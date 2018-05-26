package net.squanchy.service.firebase.model.conferenceinfo

import com.google.firebase.firestore.GeoPoint
import org.junit.Test

private const val A_STRING = "Whatever"
private const val A_COORDINATE = 5.87654

class FirestoreVenueMapperTest {

    @Test
    fun `venue name should match when mapped`() {
        val firestoreVenue = aFirestoreVenue(name = A_STRING)
        val venue = firestoreVenue.toVenue()
        assertThat(venue.name).isEqualTo(A_STRING)
    }

    @Test
    fun `venue address should match when mapped`() {
        val firestoreVenue = aFirestoreVenue(address = A_STRING)
        val venue = firestoreVenue.toVenue()
        assertThat(venue.address).isEqualTo(A_STRING)
    }

    @Test
    fun `venue description should match when mapped`() {
        val firestoreVenue = aFirestoreVenue(description = A_STRING)
        val venue = firestoreVenue.toVenue()
        assertThat(venue.description).isEqualTo(A_STRING)
    }

    @Test
    fun `venue mapUrl should match when mapped`() {
        val firestoreVenue = aFirestoreVenue(mapUrl = A_STRING)
        val venue = firestoreVenue.toVenue()
        assertThat(venue.mapUrl).isEqualTo(A_STRING)
    }

    @Test
    fun `venue lat should match when mapped`() {
        val firestoreVenue = aFirestoreVenue(latLon = GeoPoint(A_COORDINATE, 0.0))
        val venue = firestoreVenue.toVenue()
        assertThat(venue.latitude).isWithin(0.1).of(A_COORDINATE)
    }

    @Test
    fun `venue long should match when mapped`() {
        val firestoreVenue = aFirestoreVenue(latLon = GeoPoint(0.0, A_COORDINATE))
        val venue = firestoreVenue.toVenue()
        assertThat(venue.longitude).isWithin(0.1).of(A_COORDINATE)
    }
}
