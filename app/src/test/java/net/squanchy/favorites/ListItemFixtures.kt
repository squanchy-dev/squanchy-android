package net.squanchy.favorites

import net.squanchy.favorites.view.FavoritesItem
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.aDay
import net.squanchy.schedule.domain.view.anEvent
import org.joda.time.LocalDate

fun aFavoriteHeaderListItem(
    date: LocalDate = aDay().date
) = FavoritesItem.Header(date = date)

fun aFavoriteItemListItem(
    event: Event = anEvent()
) = FavoritesItem.Favorite(event = event)
