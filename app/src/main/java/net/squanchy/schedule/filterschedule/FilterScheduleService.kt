package net.squanchy.schedule.filterschedule

import net.squanchy.service.repository.FilterScheduleRepository

class FilterScheduleService(private val filterRepository: FilterScheduleRepository) {

    private val allTracks = filterRepository.allTracks
    private var currentFiltering = filterRepository.filters.value

    val trackNames
        get() = allTracks.map { it.name }.toTypedArray()

    val currentSelection: BooleanArray
        get() {
            // When the no filter is selected (all false) we return an all selected array for a consistent UX
            if (currentFiltering.isEmpty()) {
                currentFiltering = allTracks.toSet()
            }
            return allTracks.map { currentFiltering.contains(it) }.toBooleanArray()
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

    fun restore(selection: BooleanArray) {
        currentFiltering = allTracks.filterIndexed { index, _ -> selection[index] }.toSet()
    }
}
