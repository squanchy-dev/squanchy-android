package net.squanchy.schedule.filterschedule

import net.squanchy.service.repository.FilterScheduleRepository

class FilterScheduleService(private val filterRepository: FilterScheduleRepository) {

    private val allTracks = filterRepository.allTracks
    var currentFiltering = filterRepository.filters.value

    val trackNames
        get() = allTracks.map { it.name }.toTypedArray()

    val currentSelection: BooleanArray
        get() {
            val filtering = allTracks.map { currentFiltering.contains(it) }.toBooleanArray()
            // When the filter is empty (all false) we return an all selected array for a consistent UX
            if (filtering.all { !it }) {
                return BooleanArray(allTracks.size) { true }
            } else {
                return filtering
            }
        }

    fun confirm() {
        filterRepository.setFilter(currentFiltering)
    }

    fun add(position: Int) {
        currentFiltering += allTracks[position]
    }

    fun remove(position: Int) {
        currentFiltering -= allTracks[position]
    }
}
