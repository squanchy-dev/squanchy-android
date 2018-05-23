package net.squanchy.schedule

import io.reactivex.Observable
import net.squanchy.schedule.domain.view.Schedule

interface ScheduleService {

    fun schedule(): Observable<Schedule>
}
