package net.squanchy.support.time

import com.google.firebase.Timestamp
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

fun Timestamp.toInstant(): Instant = Instant.ofEpochMilli(toDate().time)
fun Timestamp.toZonedDateTime(zoneId: ZoneId): ZonedDateTime = toInstant().atZone(zoneId)
fun Timestamp.toLocalDate(zoneId: ZoneId): LocalDate = toZonedDateTime(zoneId).toLocalDate()
fun Timestamp.toLocalDateTime(zoneId: ZoneId): LocalDateTime = toZonedDateTime(zoneId).toLocalDateTime()
