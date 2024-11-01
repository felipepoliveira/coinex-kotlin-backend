package io.felipepoliveira.coinex.platformapi.rest.v1

import io.felipepoliveira.coinex.platformapi.ext.toResponseEntity
import io.felipepoliveira.coinex.services.BusinessRuleException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus


@ControllerAdvice
class ExceptionHandler {

    /**
     * Handle HttpMessageNotReadableException
     */
    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handleBadRequest(exception: HttpMessageNotReadableException) : ResponseEntity<Any> {
        return ResponseEntity
            .badRequest()
            .header("X-Error", "Http message not readable")
            .body(mutableMapOf(Pair("reason", exception.message)))
    }

    /**
     * Handle BusinessRuleException exception
     */
    @ExceptionHandler(value = [BusinessRuleException::class])
    fun handleBusinessRuleException(businessRuleException: BusinessRuleException) = businessRuleException.toResponseEntity()

    /**
     * Handles any other uncaught exceptions (e.g., Hibernate) and returns 500 Internal Server Error.
     */
    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(ex: Exception): ResponseEntity<Any> {
        ex.printStackTrace()
        return ResponseEntity
            .internalServerError()
            .header("X-Reason", "An unexpected error occur in the server")
            .build()
    }
}