package net.squanchy.service.repository.firestore

import io.reactivex.Observable
import net.squanchy.schedule.domain.view.Day
import net.squanchy.service.DaysRepository
import net.squanchy.service.firestore.FirestoreDbService
import org.joda.time.LocalDate

class FirestoreDayRepository(private val firestoreDbService: FirestoreDbService): DaysRepository {

    override fun days(): Observable<List<Day>> {
        return firestoreDbService.days().map { it.map { Day("", LocalDate()) } }
    }

}
