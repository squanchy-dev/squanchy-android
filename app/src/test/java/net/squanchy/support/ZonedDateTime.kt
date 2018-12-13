package net.squanchy.support

import org.threeten.bp.DateTimeUtils
import org.threeten.bp.ZonedDateTime
import java.util.Date

fun ZonedDateTime.toDate(): Date = DateTimeUtils.toDate(toInstant())
