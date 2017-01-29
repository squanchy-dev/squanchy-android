package com.connfa.service;

import android.content.Context;

import com.connfa.R;
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
import com.connfa.service.api.ConnfaService;
import com.connfa.service.api.model.updates.ApiUpdates;
import com.connfa.service.model.Updates;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ConnfaRepository {
    private final ConnfaService service;

    public static ConnfaRepository newInstance(Context context) {
        String baseUrl = context.getString(R.string.api_value_base_url);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
        ConnfaService service = retrofit.create(ConnfaService.class);
        return new ConnfaRepository(service);
    }

    private ConnfaRepository(ConnfaService service) {
        this.service = service;
    }

    public Observable<Updates> updates() {
        return service.updates()
                .map(toUpdates());
    }

    private Function<ApiUpdates, Updates> toUpdates() {
        return updates -> new Updates(updates.idsForUpdate);
    }

    public Observable<Event.Holder> bofs() {
        return service.bofs();
    }

    public Observable<SettingsHolder> settings() {
        return service.settings();
    }

    public Observable<FloorPlan.Holder> floorPlans() {
        return service.floorPlans();
    }

    public Observable<InfoItem.General> info() {
        return service.info();
    }

    public Observable<Level.Holder> levels() {
        return service.levels();
    }

    public Observable<Location.Holder> locations() {
        return service.locations();
    }

    public Observable<POI.Holder> pois() {
        return service.pois();
    }

    public Observable<Speaker.Holder> speakers() {
        return service.speakers();
    }

    public Observable<Track.Holder> tracks() {
        return service.tracks();
    }

    public Observable<Type.Holder> types() {
        return service.types();
    }

    public Observable<Event.Holder> socialEvents() {
        return service.socialEvents();
    }

    public Observable<Event.Holder> sessions() {
        return service.sessions();
    }
}
