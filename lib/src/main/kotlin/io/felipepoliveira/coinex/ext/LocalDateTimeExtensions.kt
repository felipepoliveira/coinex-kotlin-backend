package io.felipepoliveira.coinex.ext

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

/**
 * Parse this LocalDateTime instance to a java.util.Date instance
 */
fun LocalDateTime.toDate(zoneId: ZoneId = ZoneId.systemDefault()): Date = Date.from(this.atZone(zoneId).toInstant())