package net.squanchy.favorites.view

import io.reactivex.Observable

private const val TAPS_TO_TRIGGER_INITIAL_ACHIEVEMENT = 5
private const val TAPS_TO_TRIGGER_PERSEVERANCE_ACHIEVEMENT = 15

internal fun favoritesSignedInEmptyLayoutPresenter(clickEvent: Observable<FavoritesSignedInEmptyLayout.FavoritesClickEvent>): Observable<FavoritesEmptyViewState> {

    return clickEvent.map { clickEvent ->

        var counter = clickEvent.counter

        var filledIcon = false

        if (counter % 2 == 0) filledIcon = true

        counter++

        var fastLearner = false

        if (counter == TAPS_TO_TRIGGER_INITIAL_ACHIEVEMENT) fastLearner = true

        var perseverant = false

        if (counter == TAPS_TO_TRIGGER_PERSEVERANCE_ACHIEVEMENT) perseverant = true

        FavoritesEmptyViewState(counter, filledIcon, fastLearner, perseverant)
    }

}

internal data class FavoritesEmptyViewState(val counter: Int, val filledIcon: Boolean, val fastLearner: Boolean, val perseverant: Boolean)