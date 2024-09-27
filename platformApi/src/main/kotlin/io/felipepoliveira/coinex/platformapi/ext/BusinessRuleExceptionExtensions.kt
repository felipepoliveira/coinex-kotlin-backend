package io.felipepoliveira.coinex.platformapi.ext

import io.felipepoliveira.coinex.services.BusinessRuleException
import org.springframework.http.ResponseEntity

/**
 * Create and return a new ResponseEntity instance based on the data of this BusinessRuleException
 */
fun BusinessRuleException.toResponseEntity(): ResponseEntity<Any> {
    return ResponseEntity
        .status(this.type.httpStatus)
        .header("X-Error", this.type.toString())
        .body(this.errorDetails)
}