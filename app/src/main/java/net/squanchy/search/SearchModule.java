package net.squanchy.search;

import android.content.Context;

import net.squanchy.navigation.Navigator;
import net.squanchy.search.engines.SearchEngines;
import net.squanchy.service.repository.EventRepository;
import net.squanchy.service.repository.SpeakerRepository;

import dagger.Module;
import dagger.Provides;

@Module
class SearchModule {

    private final Context context;

    SearchModule(Context context) {
        this.context = context;
    }

    @Provides
    SearchEngines searchEngines() {
        return new SearchEngines();
    }

    @Provides
    SearchService searchService(EventRepository eventRepository, SpeakerRepository speakerRepository, SearchEngines searchEngines) {
        return new SearchService(eventRepository, speakerRepository, searchEngines);
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
