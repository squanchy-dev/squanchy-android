package net.squanchy.schedule.filterschedule

import net.squanchy.service.repository.FilterScheduleRepository

class FilterScheduleService(private val filterRepository: FilterScheduleRepository) {

    private val allTracks = filterRepository.allTracks.blockingFirst()

    val trackNames
        get() = allTracks.map { it.name }.toTypedArray()

    val currentSelection: BooleanArray
        get() = allTracks.map { currentFiltering.contains(it) }.toBooleanArray()

    var currentFiltering = filterRepository.filters.value

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
