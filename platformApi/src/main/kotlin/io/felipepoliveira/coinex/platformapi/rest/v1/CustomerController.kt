package io.felipepoliveira.coinex.platformapi.rest.v1

import io.felipepoliveira.coinex.services.BaseService
import io.felipepoliveira.coinex.services.CustomerService
import io.felipepoliveira.coinex.services.dto.customer.CreateCustomerDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/customers")
open class CustomerController @Autowired constructor(
    private val customerService: CustomerService,
) : BaseController() {

    /**
     * Register a new customer account into the platform
     */
    @PostMapping
    fun registerCustomer(@RequestBody dto: CreateCustomerDTO) = ok {
        customerService.registerCustomer(dto)
    }

}