package com.grishko188.pointofinterest.core.utils

import kotlinx.datetime.*
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

object DateTimeFormatterCache {
    private const val CACHE_SIZE = 16
    private val cache = HashMap<String, DateTimeFormatter>(CACHE_SIZE)

    operator fun get(name: String): DateTimeFormatter? = cache[name]

    operator fun set(name: String, formatter: DateTimeFormatter) {
        cache[name] = formatter
    }

    fun getOrCreateFormatter(pattern: String, locale: Locale = Locale.getDefault()): DateTimeFormatter {
        val key = "$pattern${locale.displayName ?: ""}"
        var format = this[key]
        if (format == null) {
            format = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault())
            this[key] = format
        }
        return checkNotNull(format)
    }
}

/**
 * Simple class which contains number of common and wide useful date formats.
 * @author Grishko Nikita
 */
object CommonFormats {
    /**
     * Date/time format: dd.MM.yyyy HH:mm:ss
     */
    const val FORMAT_SIMPLE_DATE_TIME_SECONDS = "dd.MM.yyyy HH:mm:ss"
    /**
     * Date/time format: dd.MM.yyyy HH:mm
     */
    const val FORMAT_SIMPLE_DATE_TIME = "dd.MM.yyyy HH:mm"
    /**
     * Date/time format: dd.MM.yyyy
     */
    const val FORMAT_SIMPLE_DATE = "dd.MM.yyyy"
    /**
     * Date/time format: dd LLL yyyy
     */
    const val FORMAT_DATE_WITH_MONTH_NAME = "dd LLL yyyy"

    /**
     * Date/time format: dd LLL yyyy
     */
    const val FORMAT_DATE_WITH_MONTH_NAME_AND_TIME = "dd LLL yyyy, HH:mm "
    /**
     * Date/time format: yyyy-MM-dd HH:mm:ss
     */
    const val FORMAT_STANDARD_DATE_TIME_SECONDS = "yyyy-MM-dd HH:mm:ss"
    /**
     * Date/time format: yyyy-MM-dd HH:mm
     */
    const val FORMAT_STANDARD_DATE_TIME = "yyyy-MM-dd HH:mm"
    /**
     * Date/time format: yyyy-MM-dd
     */
    const val FORMAT_STANDARD_DATE = "yyyy-MM-dd"
    /**
     * Date/time format: yyyy-MM-dd'T'HH:mm:ss'Z'
     */
    const val FORMAT_STANDARD_DATE_FULL_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    /**
     * Date/time format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     */
    const val FORMAT_STANDARD_DATE_FULL_MILLIS_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    /**
     * Date/time format: HH:mm
     */
    const val FORMAT_TIME = "HH:mm"
    /**
     * Date/time format: HH:mm:ss
     */
    const val FORMAT_TIME_2 = "HH:mm:ss"
}

/**
 * Format [Instant] to date/time [String] with given format pattern.
 * DateTimeFormatter objects will be created once and cached by params [pattern] and [locale].
 * For different params will be cached different objects
 *
 * @param pattern the pattern describing the date and time format
 * @param locale the locale whose date format symbols should be used. Can be null
 *
 * @return formatted date/time [String]
 */
fun Instant.toStringWithFormat(pattern: String, locale: Locale = Locale.getDefault()): String {
    return DateTimeFormatterCache.getOrCreateFormatter(pattern, locale)
        .format(this.toJavaInstant())
}

/**
 * Format [Instant] to date/time [String] with given instance of [DateTimeFormatter] without caching.
 *
 * @param format instance of [DateTimeFormatter]
 *
 * @return formatted date/time [String]
 */
fun Instant.toStringWithFormat(format: DateTimeFormatter): String =
    format.format(this.toJavaInstant())