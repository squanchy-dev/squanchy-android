package net.squanchy.home

import androidx.annotation.StyleRes

import net.squanchy.R

enum class BottomNavigationSection(@param:StyleRes val theme: Int) {
    SCHEDULE(R.style.Theme_Squanchy_Schedule),
    FAVORITES(R.style.Theme_Squanchy_Favorites),
    TWEETS(R.style.Theme_Squanchy_Tweets),
    VENUE_INFO(R.style.Theme_Squanchy_VenueInfo);
}
