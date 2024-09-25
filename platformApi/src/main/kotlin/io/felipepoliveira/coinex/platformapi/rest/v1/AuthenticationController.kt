package io.felipepoliveira.coinex.platformapi.rest.v1

import io.felipepoliveira.coinex.platformapi.security.AuthenticationTokenHandler
import io.felipepoliveira.coinex.services.ServiceRequester
import io.felipepoliveira.coinex.services.UserService
import io.felipepoliveira.coinex.services.dto.users.FindByEmailAndPasswordDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/v1/auth")
class AuthenticationController @Autowired constructor(
    private val authenticationTokenHandler: AuthenticationTokenHandler,
    private val userService: UserService,
) : BaseController() {

    @PostMapping("/public/tokens/generate-with-email-and-password")
    fun generateTokenWithEmailAndPassword(@RequestBody dto: FindByEmailAndPasswordDTO) = ok {
        // Fetch the user using email and password combination
        val user = userService.findByPrimaryEmailAndPassword(dto)

        // issue the authentication token
        authenticationTokenHandler.issue(user, LocalDateTime.now().plusDays(7))
    }

    /**
     * Return information about the authenticated user
     */
    @GetMapping("/me")
    fun me(@AuthenticationPrincipal serviceRequester: ServiceRequester) = ok {
        userService.assertFindByUuid(serviceRequester.userUuid)
    }
}