package io.felipepoliveira.coinex.platformapi.rest.v1

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException
import io.felipepoliveira.coinex.platformapi.ext.toResponseEntity
import io.felipepoliveira.coinex.services.BusinessRuleException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.Exception

@ControllerAdvice
class ExceptionHandler {

    /**
     * Handle BusinessRuleException exception
     */
    @ExceptionHandler(value = [BusinessRuleException::class])
    fun handleBusinessRuleException(businessRuleException: BusinessRuleException) = businessRuleException.toResponseEntity()
}