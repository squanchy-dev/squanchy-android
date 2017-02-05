package net.squanchy.service.api;

import android.content.Context;

import net.squanchy.R;
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
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
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
        return service.updates();
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
