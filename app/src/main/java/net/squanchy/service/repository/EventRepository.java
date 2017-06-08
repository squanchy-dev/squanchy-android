package net.squanchy.service.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.squanchy.eventdetails.domain.view.ExperienceLevel;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.schedule.domain.view.Place;
import net.squanchy.schedule.domain.view.Track;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseEvents;
import net.squanchy.service.firebase.model.FirebaseFavorites;
import net.squanchy.service.firebase.model.FirebasePlaces;
import net.squanchy.service.firebase.model.FirebaseTracks;
import net.squanchy.service.firebase.model.FirebaseVenue;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.support.lang.Checksum;
import net.squanchy.support.lang.Func1;
import net.squanchy.support.lang.Lists;
import net.squanchy.support.lang.Optional;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function6;
import io.reactivex.schedulers.Schedulers;

import static net.squanchy.support.lang.Lists.filter;

public class EventRepository {

    private final FirebaseDbService dbService;
    private final Checksum checksum;
    private final SpeakerRepository speakerRepository;

    public EventRepository(FirebaseDbService dbService, Checksum checksum, SpeakerRepository speakerRepository) {
        this.dbService = dbService;
        this.checksum = checksum;
        this.speakerRepository = speakerRepository;
    }

    public Observable<Event> event(String eventId, String userId) {
        Observable<FirebaseEvent> eventObservable = dbService.event(eventId);
        Observable<List<Speaker>> speakersObservable = speakerRepository.speakers();
        Observable<FirebaseFavorites> favoritesObservable = dbService.favorites(userId);
        Observable<List<Place>> placesObservable = dbService.places().map(toPlaces());                 // TODO access them by ID directly?
        Observable<List<Track>> tracksObservable = dbService.tracks().map(toTracks());                 // TODO extract repositories?
        Observable<DateTimeZone> timeZoneObservable = dbService.venueInfo().map(toTimeZone());

        return Observable.combineLatest(
                eventObservable,
                speakersObservable,
                favoritesObservable,
                placesObservable,
                tracksObservable,
                timeZoneObservable,
                combineIntoEvent()
        ).subscribeOn(Schedulers.io());
    }

    private Function<FirebaseVenue, DateTimeZone> toTimeZone() {
        return firebaseVenue -> DateTimeZone.forID(firebaseVenue.getTimezone());
    }

    private Function<FirebasePlaces, List<Place>> toPlaces() {
        return firebasePlaces -> Lists.map(
                firebasePlaces.getPlaces(),
                firebasePlace -> Place.Companion.create(
                        firebasePlace.getId(),
                        firebasePlace.getName(),
                        Optional.fromNullable(firebasePlace.getFloor())
                )
        );
    }

    private Function<FirebaseTracks, List<Track>> toTracks() {
        return firebaseTracks -> Lists.map(
                firebaseTracks.getTracks(),
                firebaseTrack -> Track.Companion.create(
                        firebaseTrack.getId(),
                        firebaseTrack.getName(),
                        Optional.fromNullable(firebaseTrack.getAccent_color()),
                        Optional.fromNullable(firebaseTrack.getText_color()),
                        Optional.fromNullable(firebaseTrack.getIcon_url())
                )
        );
    }

    private Function6<FirebaseEvent, List<Speaker>, FirebaseFavorites, List<Place>, List<Track>, DateTimeZone, Event> combineIntoEvent() {
        return (apiEvent, speakers, favorites, places, tracks, timeZone) -> Event.Companion.create(
                apiEvent.getId(),
                checksum.getChecksumOf(apiEvent.getId()),
                apiEvent.getDay_id(),
                new LocalDateTime(apiEvent.getStart_time()),
                new LocalDateTime(apiEvent.getEnd_time()),
                apiEvent.getName(),
                placeById(places, apiEvent.getPlace_id()),
                Optional.fromNullable(apiEvent.getExperience_level()).flatMap(ExperienceLevel::fromNullableRawLevel),
                speakersByIds(speakers, apiEvent.getSpeaker_ids()),
                Event.Type.Companion.fromRawType(apiEvent.getType()),
                favorites.hasFavorite(apiEvent.getId()),
                Optional.fromNullable(apiEvent.getDescription()),
                trackById(tracks, apiEvent.getTrack_id()),
                timeZone
        );
    }

    private Optional<Place> placeById(List<Place> places, String placeId) {
        return Lists.find(places, place -> place.getId().equals(placeId));
    }

    private Optional<Track> trackById(List<Track> tracks, String trackId) {
        return Lists.find(tracks, track -> track.getId().equals(trackId));
    }

    public Observable<List<Event>> events(String userId) {
        Observable<FirebaseEvents> sessionsObservable = dbService.events();
        Observable<List<Speaker>> speakersObservable = speakerRepository.speakers();
        Observable<FirebaseFavorites> favoritesObservable = dbService.favorites(userId);
        Observable<List<Place>> placesObservable = dbService.places().map(toPlaces());                 // TODO access them by ID directly?
        Observable<List<Track>> tracksObservable = dbService.tracks().map(toTracks());                 // TODO extract repositories?
        Observable<DateTimeZone> timeZoneObservable = dbService.venueInfo().map(toTimeZone());

        return Observable.combineLatest(
                sessionsObservable,
                speakersObservable,
                favoritesObservable,
                placesObservable,
                tracksObservable,
                timeZoneObservable,
                combineIntoEvents()
        );
    }

    private Function6<FirebaseEvents, List<Speaker>, FirebaseFavorites, List<Place>, List<Track>, DateTimeZone, List<Event>> combineIntoEvents() {
        return (firebaseEvents, speakers, favorites, places, tracks, timeZone) ->
                Lists.map(new ArrayList<>(firebaseEvents.getEvents().values()), combineEventWith(speakers, favorites, places, tracks, timeZone));
    }

    private Func1<FirebaseEvent, Event> combineEventWith(
            List<Speaker> speakers,
            FirebaseFavorites favorites,
            List<Place> places,
            List<Track> tracks,
            DateTimeZone timeZone
    ) {
        return apiEvent -> Event.Companion.create(
                apiEvent.getId(),
                checksum.getChecksumOf(apiEvent.getId()),
                apiEvent.getDay_id(),
                new LocalDateTime(apiEvent.getStart_time()),
                new LocalDateTime(apiEvent.getEnd_time()),
                apiEvent.getName(),
                placeById(places, apiEvent.getPlace_id()),
                Optional.fromNullable(apiEvent.getExperience_level()).flatMap(ExperienceLevel::fromNullableRawLevel),
                speakersByIds(speakers, apiEvent.getSpeaker_ids()),
                Event.Type.Companion.fromRawType(apiEvent.getType()),
                favorites.hasFavorite(apiEvent.getId()),
                Optional.fromNullable(apiEvent.getDescription()),
                trackById(tracks, apiEvent.getTrack_id()),
                timeZone
        );
    }

    private List<Speaker> speakersByIds(List<Speaker> speakers, List<String> speakerIds) {
        if (speakerIds == null || speakerIds.isEmpty()) {
            return Collections.emptyList();
        }

        return filter(speakers, speaker -> speakerIds.contains(speaker.getId()));
    }

    public Completable addFavorite(String eventId, String userId) {
        return dbService.addFavorite(eventId, userId);
    }

    public Completable removeFavorite(String eventId, String userId) {
        return dbService.removeFavorite(eventId, userId);
    }
}
