package net.squanchy.service.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers
import net.squanchy.service.firebase.model.FirebaseAchievements
import net.squanchy.service.firebase.model.FirebaseDays
import net.squanchy.service.firebase.model.FirebaseEvent
import net.squanchy.service.firebase.model.FirebaseEvents
import net.squanchy.service.firebase.model.FirebaseFavorites
import net.squanchy.service.firebase.model.FirebasePlace
import net.squanchy.service.firebase.model.FirebasePlaces
import net.squanchy.service.firebase.model.FirebaseSpeakers
import net.squanchy.service.firebase.model.FirebaseTrack
import net.squanchy.service.firebase.model.FirebaseTracks
import net.squanchy.service.firebase.model.FirebaseUserData
import net.squanchy.service.firebase.model.FirebaseVenue
import net.squanchy.support.lang.Optional

class FirebaseDbService(private val database: DatabaseReference) {

    fun days(): Observable<FirebaseDays> {
        return observeChild(daysNode(), FirebaseDays::class.java)
    }

    fun speakers(): Observable<FirebaseSpeakers> {
        return observeChild(speakersNode(), FirebaseSpeakers::class.java)
    }

    fun events(): Observable<FirebaseEvents> {
        return observeChild(eventsNode(), FirebaseEvents::class.java)
    }

    fun event(eventId: String): Observable<FirebaseEvent> {
        return observeChild(eventByIdNode(eventId), FirebaseEvent::class.java)
    }

    fun places(): Observable<FirebasePlaces> {
        return observeChild(placesNode(), FirebasePlaces::class.java)
    }

    fun place(placeId: String): Observable<FirebasePlace> {
        return observeChild(placeByIdNode(placeId), FirebasePlace::class.java)
    }

    fun tracks(): Observable<FirebaseTracks> {
        return observeChild(tracksNode(), FirebaseTracks::class.java)
    }

    fun track(trackId: String): Observable<FirebaseTrack> {
        return observeChild(trackByIdNode(trackId), FirebaseTrack::class.java)
    }

    fun favorites(userId: String): Observable<FirebaseFavorites> {
        return userData(userId)
                .map { optionalUserData -> optionalUserData.or(FirebaseUserData.empty()) }
                .map { userData -> Optional.fromNullable(userData.favorites) }
                .map { optionalFavorites -> optionalFavorites.or(FirebaseFavorites.empty()) }
                .map(::FirebaseFavorites)
    }

    fun achievements(userId: String): Observable<FirebaseAchievements> {
        return userData(userId)
                .map { optionalUserData -> optionalUserData.or(FirebaseUserData.empty()) }
                .map { userData -> Optional.fromNullable(userData.achievements) }
                .map { optionalAchievement -> optionalAchievement.or(FirebaseAchievements.empty()) }
                .map(::FirebaseAchievements)
    }

    private fun userData(userId: String): Observable<Optional<FirebaseUserData>> {
        return observeOptionalChild(userDataNode(userId), FirebaseUserData::class.java)
    }

    fun venueInfo(): Observable<FirebaseVenue> {
        return observeChild(venudInfoNode(), FirebaseVenue::class.java)
    }

    private fun <T> observeChild(path: String, clazz: Class<T>): Observable<T> {
        return observeChildAndEmit(path, clazz, { value: T -> value })
    }

    private fun <T> observeOptionalChild(path: String, clazz: Class<T>): Observable<Optional<T>> {
        return observeChildAndEmit(path, clazz, { Optional.fromNullable(it) })
    }

    private fun <T, V> observeChildAndEmit(path: String, clazz: Class<V>, map: (V) -> T): Observable<T> {
        return Observable.create { emitter: ObservableEmitter<T> ->
            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (emitter.isDisposed) {
                        return
                    }
                    val value = dataSnapshot.getValue(clazz)
                    emitter.onNext(map(value))
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    if (emitter.isDisposed) {
                        return
                    }
                    emitter.onError(databaseError.toException())
                }
            }

            val childReference = database.child(path)
            childReference.addValueEventListener(listener)
            emitter.setCancellable { childReference.removeEventListener(listener) }
        }.observeOn(Schedulers.io())
    }

    fun addFavorite(eventId: String, userId: String): Completable {
        return updateFavorite(eventId, { it.setValue(true) }, userId)
    }

    fun removeFavorite(eventId: String, userId: String): Completable {
        return updateFavorite(eventId, { it.removeValue() }, userId)
    }

    private fun updateFavorite(eventId: String, action: (DatabaseReference) -> Task<Void>, userId: String): Completable {
        return Completable.create { emitter ->
            action(database.child(favoriteByIdNode(userId, eventId)))
                    .addOnSuccessListener { emitter.onComplete() }
                    .addOnFailureListener { e ->
                        if (emitter.isDisposed) {
                            return@addOnFailureListener
                        }

                        emitter.onError(e)
                    }
        }
    }

    fun addAchievement(userId: String, achievementId: String, timestamp: Long?): Completable {
        return Completable.create { emitter ->
            database.child(achievementByIdNode(userId, achievementId)).setValue(timestamp)
                    .addOnSuccessListener { emitter.onComplete() }
                    .addOnFailureListener { e ->
                        if (emitter.isDisposed) {
                            return@addOnFailureListener
                        }

                        emitter.onError(e)
                    }
        }
    }
}

private fun data(node: String) = "data/$node"
private fun daysNode() = data("days")
private fun speakersNode() = data("speakers")
private fun eventsNode() = data("events")
private fun eventByIdNode(eventId: String) = data("events/$eventId")
private fun placesNode() = data("places")
private fun placeByIdNode(placeId: String) = data("places/$placeId")
private fun tracksNode() = data("tracks")
private fun trackByIdNode(trackId: String) = data("tracks/$trackId")
private fun venudInfoNode() = data("venue")
private fun userDataNode(userId: String) = data("user/$userId")
private fun favoriteByIdNode(userId: String, eventId: String) = "${userDataNode(userId)}/favorites/$eventId"
private fun achievementByIdNode(userId: String, achievementId: String) = "${userDataNode(userId)}/achievements/$achievementId"
