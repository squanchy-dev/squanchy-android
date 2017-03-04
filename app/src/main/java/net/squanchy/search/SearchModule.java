package net.squanchy.search;

import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.injection.DbServiceType;

import dagger.Module;
import dagger.Provides;

@Module
class SearchModule {

    @Provides
    SearchService searchService(@DbServiceType(DbServiceType.Type.AUTHENTICATED) FirebaseDbService dbService) {
        return new SearchService(dbService);
    }
}
