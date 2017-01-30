package com.connfa.service.api;

import com.connfa.model.data.Event;
import com.connfa.model.data.FloorPlan;
import com.connfa.model.data.InfoItem;
import com.connfa.model.data.Level;
import com.connfa.model.data.Location;
import com.connfa.model.data.POI;
import com.connfa.model.data.SettingsHolder;
import com.connfa.model.data.Speaker;
import com.connfa.model.data.Track;
import com.connfa.model.data.Type;
import com.connfa.service.model.Updates;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ConnfaService {
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
