package io.felipepoliveira.coinex.ext

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

fun Date.toLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return this.toInstant()
        .atZone(zoneId)
        .toLocalDateTime()
}