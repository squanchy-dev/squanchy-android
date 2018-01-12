package net.squanchy.service.repository.firebase

import io.reactivex.Observable
import net.squanchy.schedule.domain.view.Day
import net.squanchy.service.DaysRepository
import net.squanchy.service.firebase.FirebaseDbService
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

class FirebaseDaysRepository(private val firebaseDbService: FirebaseDbService) : DaysRepository {

    companion object {
        private val DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd")
    }

    override fun days(): Observable<List<Day>> = firebaseDbService.days()
        .map { it.days?.map { Day(it.id!!, it.date!!.asLocalDate()) } ?: emptyList() }

    private fun String.asLocalDate() = LocalDate.parse(this, DATE_FORMATTER)
}
