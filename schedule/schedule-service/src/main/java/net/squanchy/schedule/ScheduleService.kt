package net.squanchy.schedule

import io.reactivex.Observable


interface ScheduleService {

    fun schedule(): Observable<Schedule>
}