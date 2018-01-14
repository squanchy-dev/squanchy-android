package net.squanchy.home.deeplink

import android.content.Intent
import android.os.Bundle

import net.squanchy.home.BottomNavigationSection
import net.squanchy.home.HomeStatePersister

import net.squanchy.home.deeplink.HomeActivityDeepLinkCreator.KEY_INITIAL_PAGE_INDEX

class HomeActivityIntentParser(private val savedState: Bundle?, private val intent: Intent) {

    private val statePersister: HomeStatePersister = HomeStatePersister()

    val initialSelectedPage: BottomNavigationSection
        get() = getSectionFrom(statePersister, savedState)
                ?: getSectionFrom(intent.extras)
                ?: BottomNavigationSection.SCHEDULE

    private fun getSectionFrom(statePersister: HomeStatePersister, savedState: Bundle?): BottomNavigationSection? {
        return statePersister.readSectionFrom(savedState)
    }

    private fun getSectionFrom(intentExtras: Bundle?): BottomNavigationSection? {
        val bundle = intentExtras ?: Bundle.EMPTY

        if (bundle.containsKey(KEY_INITIAL_PAGE_INDEX)) {
            val sectionIndex = bundle.getInt(KEY_INITIAL_PAGE_INDEX)
            return BottomNavigationSection.values()[sectionIndex]
        } else {
            return null
        }
    }
}
