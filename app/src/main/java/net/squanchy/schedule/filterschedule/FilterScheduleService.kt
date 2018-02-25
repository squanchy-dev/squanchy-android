package net.squanchy.schedule.filterschedule

import net.squanchy.service.repository.FilterScheduleRepository

class FilterScheduleService(private val filterRepository: FilterScheduleRepository) {

    private val allTracks = filterRepository.allTracks
    var currentFiltering = filterRepository.filters.value

    val trackNames
        get() = allTracks.map { it.name }.toTypedArray()

    val currentSelection: BooleanArray
        get() = allTracks.map { currentFiltering.contains(it) }.toBooleanArray()

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
