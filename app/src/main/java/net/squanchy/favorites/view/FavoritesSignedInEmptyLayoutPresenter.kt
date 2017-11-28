package net.squanchy.favorites.view

private const val TAPS_TO_TRIGGER_INITIAL_ACHIEVEMENT = 5
private const val TAPS_TO_TRIGGER_PERSEVERANCE_ACHIEVEMENT = 15

private typealias AchievementMessageProvider = () -> String
private typealias ResIdProvider = () -> Int

internal fun presentButtonIcon(counter: Int, view: FavoritesSignedInEmptyLayoutView, filledIconId: ResIdProvider, emptyIconId: ResIdProvider) {

    if (counter % 2 == 0) view.setButtonImage(filledIconId()) else view.setButtonImage(emptyIconId())

}

internal fun presentAchievementMessage(counter: Int, view: FavoritesSignedInEmptyLayoutView, initialAchievementMessage: AchievementMessageProvider,
        perseveranceMessage: AchievementMessageProvider) {

    val newCounter = counter + 1

    view.updateCounter(newCounter)

    if (newCounter == TAPS_TO_TRIGGER_INITIAL_ACHIEVEMENT) view.showAchievement(initialAchievementMessage())
    else if (newCounter == TAPS_TO_TRIGGER_PERSEVERANCE_ACHIEVEMENT) view.showAchievement(perseveranceMessage())

}

