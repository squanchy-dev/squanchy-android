package net.squanchy.support.time

import org.threeten.bp.format.DateTimeFormatter

fun createShortTimeFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")

fun createShortDateFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

fun createWeekDayAndDayFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern("EEE d")
