package net.squanchy.home

import android.os.Bundle

class HomeStatePersister {

    fun saveCurrentSection(outState: Bundle, currentSection: BottomNavigationSection) {
        outState.putInt(STATE_KEY_SELECTED_PAGE_INDEX, currentSection.ordinal)
    }

    fun readSectionFrom(savedState: Bundle?): BottomNavigationSection? {
        val bundle = savedState ?: Bundle.EMPTY

        if (bundle.containsKey(STATE_KEY_SELECTED_PAGE_INDEX)) {
            val sectionIndex = bundle.getInt(STATE_KEY_SELECTED_PAGE_INDEX)
            return BottomNavigationSection.values()[sectionIndex]
        } else {
            return null
        }
    }

    companion object {

        private const val STATE_KEY_SELECTED_PAGE_INDEX = "HomeActivity.selected_page_index"
    }
}
