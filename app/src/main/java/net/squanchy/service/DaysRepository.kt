package net.squanchy.service

import io.reactivex.Observable
import net.squanchy.schedule.domain.view.Day

interface DaysRepository {
    fun days(): Observable<List<Day>>
}
