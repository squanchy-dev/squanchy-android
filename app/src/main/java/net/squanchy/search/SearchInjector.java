package net.squanchy.search;

import net.squanchy.injection.ApplicationInjector;

final class SearchInjector {

    private SearchInjector() {
        // no instances
    }

    public static SearchComponent obtain(SearchActivity activity) {
        return DaggerSearchComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .searchModule(new SearchModule())
                .build();
    }
}
