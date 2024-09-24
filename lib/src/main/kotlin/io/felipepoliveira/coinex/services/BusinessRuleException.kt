package io.felipepoliveira.coinex.services

import org.springframework.validation.BindingResult

/**
 * Parse a BindingResult into a Map<String, String>
 */
private fun bindingResultToMap(bindingResult: BindingResult): Map<String, String> {
    val mapOfBindingResult = mutableMapOf<String, String>()
    for(fieldError in bindingResult.fieldErrors) {
        mapOfBindingResult[fieldError.field] = fieldError.defaultMessage ?: "This field has a unidentified validation error"
    }

    return mapOfBindingResult
}

class BusinessRuleException(
    val type: BusinessRuleExceptionType,
    val reason: String,
    val httpStatusCode: Int = type.httpStatus,
    val errorDetails: Map<String, String>? = null,
) : Exception(reason) {

    /**
     * Parse a BindingResult into a valid instance of BusinessRuleException where
     * `type = BusinessRuleExceptionType.DataValidation`, `reason = "Data Validation"`and error
     * `errorDetails = bindingResultToMap(bindingResult)`
     */
    constructor(bindingResult: BindingResult, reason: String = "Data validation") : this(
        type = BusinessRuleExceptionType.DataValidation,
        reason = reason,
        errorDetails = bindingResultToMap(bindingResult)
    )

}

enum class BusinessRuleExceptionType(
    val httpStatus: Int
) {
    DataValidation(httpStatus = 422),
    InvalidCredentials(httpStatus = 403),
    InvalidEmail(httpStatus = 403),
    InvalidPassword(httpStatus = 403),
    NotFound(httpStatus = 404)
}

