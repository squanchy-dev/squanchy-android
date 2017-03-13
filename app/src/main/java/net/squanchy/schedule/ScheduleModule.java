package net.squanchy.schedule;

import android.content.Context;

import net.squanchy.navigation.Navigator;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.injection.DbServiceType;
import net.squanchy.service.repository.EventRepository;

import dagger.Module;
import dagger.Provides;

@Module
class ScheduleModule {

    private final Context context;

    ScheduleModule(Context context) {
        this.context = context;
    }

    @Provides
    ScheduleService scheduleService(@DbServiceType(DbServiceType.Type.AUTHENTICATED) FirebaseDbService dbService, EventRepository eventRepository) {
        return new ScheduleService(dbService, eventRepository);
    }

    @Provides
    Context context() {
        return context;
    }

    @Provides
    Navigator navigator(Context context) {
        return new Navigator(context);
    }
}
