package net.squanchy.support.time

import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

fun LocalDateTime.toEpochMilli(zoneId: ZoneId) = atZone(zoneId).toInstant().toEpochMilli()
