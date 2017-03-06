package net.squanchy.search;

import net.squanchy.service.repository.EventRepository;
import net.squanchy.service.repository.SpeakerRepository;

import dagger.Module;
import dagger.Provides;

@Module
class SearchModule {

    @Provides
    SearchService searchService(EventRepository eventRepository, SpeakerRepository speakerRepository) {
        return new SearchService(eventRepository, speakerRepository);
    }
}
