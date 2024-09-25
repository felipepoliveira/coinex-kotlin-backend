package io.felipepoliveira.coinex.platformapi.rest.v1

import org.springframework.http.ResponseEntity

abstract class BaseController {

    /**
     * Return a response
     */
    fun <T> ok(callback: (responseBuilder: ResponseEntity.BodyBuilder) -> T): ResponseEntity<T> {
        val responseEntityBodyBuilder = ResponseEntity.status(200)
        val body = callback(responseEntityBodyBuilder)
        return responseEntityBodyBuilder.body(body)
    }
}