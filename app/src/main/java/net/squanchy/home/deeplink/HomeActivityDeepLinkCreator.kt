package net.squanchy.home.deeplink

import android.content.Context
import android.content.Intent

import net.squanchy.home.BottomNavigationSection
import net.squanchy.home.HomeActivity

import timber.log.Timber

class HomeActivityDeepLinkCreator(private val context: Context) {

    fun deepLinkTo(section: BottomNavigationSection): Builder {
        return Builder(context).withSection(section)
    }

    class Builder internal constructor(private val context: Context) {

        private lateinit var section: BottomNavigationSection
        private var dayId: String? = null
        private var eventId: String? = null

        internal fun withSection(section: BottomNavigationSection): Builder {
            this.section = section
            return this
        }

        fun withDayId(dayId: String?): Builder {
            this.dayId = dayId
            return this
        }

        fun withEventId(eventId: String?): Builder {
            this.eventId = eventId
            return this
        }

        fun build(): Intent {
            val intent = createIntentForSection(context, section)
            return maybeAddIdsTo(intent)
        }

        private fun createIntentForSection(context: Context, section: BottomNavigationSection): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(KEY_INITIAL_PAGE_INDEX, section.ordinal)
            return intent
        }

        private fun maybeAddIdsTo(intent: Intent): Intent {
            return when {
                canHaveIds(section) -> addIdsTo(intent)
                hasAtLeastOneId() -> error("The section $section does not support having IDs, and yet IDs were attached to the builder")
                else -> intent
            }
        }

        private fun canHaveIds(section: BottomNavigationSection?): Boolean {
            return section === BottomNavigationSection.SCHEDULE
        }

        private fun hasAtLeastOneId() = dayId != null || eventId != null

        private fun addIdsTo(intent: Intent): Intent {
            val intentWithIds = Intent(intent)
            if (dayId != null) {
                intentWithIds.putExtra(KEY_DAY_ID, dayId)
                if (eventId != null) {
                    intentWithIds.putExtra(KEY_EVENT_ID, eventId)
                }
            } else if (eventId != null) {
                Timber.e("Invalid deeplink containing an EventId $eventId but no DayId")
            }
            return intentWithIds
        }
    }

    companion object {

        internal const val KEY_INITIAL_PAGE_INDEX = "HomeActivity.initial_page_index"
        internal const val KEY_DAY_ID = "HomeActivity.day_id"
        internal const val KEY_EVENT_ID = "HomeActivity.event_id"
    }
}
