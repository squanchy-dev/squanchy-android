package net.squanchy.support.time

import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

fun createShortTimeFormatter(locale: Locale = Locale.getDefault()): DateTimeFormatter =
    DateTimeFormatter.ofPattern("h:mm a").withLocale(locale)

fun createShortDateFormatter(locale: Locale = Locale.getDefault()): DateTimeFormatter =
    DateTimeFormatter.ofPattern("MM/dd/yyyy").withLocale(locale)

fun createWeekDayAndDayFormatter(locale: Locale = Locale.getDefault()): DateTimeFormatter =
    DateTimeFormatter.ofPattern("EEE d").withLocale(locale)
