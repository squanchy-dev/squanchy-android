package net.squanchy.service.api;

import net.squanchy.model.data.Event;
import net.squanchy.model.data.FloorPlan;
import net.squanchy.model.data.InfoItem;
import net.squanchy.model.data.Level;
import net.squanchy.model.data.Location;
import net.squanchy.model.data.POI;
import net.squanchy.model.data.SettingsHolder;
import net.squanchy.model.data.Speaker;
import net.squanchy.model.data.Track;
import net.squanchy.model.data.Type;
import net.squanchy.service.model.Updates;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface SquanchyService {
    @GET("checkUpdates")
    Observable<Updates> updates();

    @GET("getBofs")
    Observable<Event.Holder> bofs();

    @GET("getSettings")
    Observable<SettingsHolder> settings();

    @GET("getFloorPlans")
    Observable<FloorPlan.Holder> floorPlans();

    @GET("getInfo")
    Observable<InfoItem.General> info();

    @GET("getLevels")
    Observable<Level.Holder> levels();

    @GET("getLocations")
    Observable<Location.Holder> locations();

    @GET("getPOI")
    Observable<POI.Holder> pois();

    @GET("getSpeakers")
    Observable<Speaker.Holder> speakers();

    @GET("getTracks")
    Observable<Track.Holder> tracks();

    @GET("getTypes")
    Observable<Type.Holder> types();

    @GET("getSocialEvents")
    Observable<Event.Holder> socialEvents();

    @GET("getSessions")
    Observable<Event.Holder> sessions();
}
