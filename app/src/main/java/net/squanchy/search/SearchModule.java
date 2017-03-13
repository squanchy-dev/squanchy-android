package net.squanchy.search;

import net.squanchy.search.engines.SearchEngines;
import net.squanchy.service.repository.EventRepository;
import net.squanchy.service.repository.SpeakerRepository;

import dagger.Module;
import dagger.Provides;

@Module
class SearchModule {

    @Provides
    SearchEngines searchEngines() {
        return new SearchEngines();
    }

    @Provides
    SearchService searchService(EventRepository eventRepository, SpeakerRepository speakerRepository, SearchEngines searchEngines) {
        return new SearchService(eventRepository, speakerRepository, searchEngines);
    }
}
