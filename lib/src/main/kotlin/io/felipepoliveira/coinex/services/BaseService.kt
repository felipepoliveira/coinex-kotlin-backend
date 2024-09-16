package io.felipepoliveira.coinex.services

import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.SmartValidator

abstract class BaseService(
    private val validator: SmartValidator
) {

    fun validate(obj: Any, objectName: String = "object"): BindingResult {
        val bindingResult = BeanPropertyBindingResult(obj, objectName)
        validator.validate(obj, bindingResult)

        return bindingResult
    }
}

/**
 * Add field error
 */
fun BindingResult.addError(fieldName: String, errorMessage: String) {
    this.addError(FieldError(this.objectName, fieldName, errorMessage))
}