package io.felipepoliveira.coinex.platformapi.rest.v1

import io.felipepoliveira.coinex.platformapi.rest.v1.dto.SendPasswordRecoveryEmailDTO
import io.felipepoliveira.coinex.platformapi.security.AuthenticationTokenHandler
import io.felipepoliveira.coinex.platformapi.security.RequestClient
import io.felipepoliveira.coinex.platformapi.security.SecurityRoles
import io.felipepoliveira.coinex.services.ServiceRequester
import io.felipepoliveira.coinex.services.UserService
import io.felipepoliveira.coinex.services.dto.users.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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

    /**
     * Change the current password using the current one as authentication method
     */
    @PutMapping("/me/password")
    @Secured(SecurityRoles.STL_MOST_RECENT)
    fun changePassword(
        @AuthenticationPrincipal requestClient: RequestClient,
        @RequestBody dto: ChangePasswordUsingCurrentPasswordAsAuthenticationDTO
    ) = ok {
        userService.changePasswordUsingCurrentPasswordAsAuthentication(requestClient.serviceRequester, dto)
    }

    /**
     * Change the password using password recovery token
     */
    @PutMapping("/public/change-password-using-recovery-token")
    fun changePasswordUsingRecoveryToken(@RequestBody dto: ChangePasswordUsingRecoveryTokenDTO) = ok {
        userService.changePasswordUsingRecoveryToken(dto)
    }

    /**\
     * Change the primary email using email token
     */
    @PutMapping("/me/primary-email")
    fun changePrimaryEmailWithToken(
        @AuthenticationPrincipal requestClient: RequestClient,
        @RequestBody dto: ChangePrimaryEmailUsingTokenAndSecretCodeDTO
    ) = ok {
        userService.changePrimaryEmailUsingTokenAndSecretCode(requestClient.serviceRequester, dto)
    }

    /**
     * Generate an authentication token using email and password combination
     */
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
    fun me(@AuthenticationPrincipal requestClient: RequestClient) = ok {
        userService.assertFindByUuid(requestClient.serviceRequester.userUuid)
    }

    @PostMapping("/send-email-confirmation-email")
    fun sendEmailConfirmationMail(@AuthenticationPrincipal requestClient: RequestClient) = ok {
        userService.sendEmailConfirmationEmail(requestClient.serviceRequester)
    }

    /**
     * Send the password recovery email to the given email
     */
    @PostMapping("/public/send-password-recovery-email")
    fun sendPasswordRecoveryEmail(@RequestBody dto: SendPasswordRecoveryEmailDTO) = ok {
        userService.sendPasswordRecoveryEmail(dto.email)
    }

    /**
     * Send the primary email change email
     */
    @PostMapping("/send-primary-email-change-email")
    fun sendPrimaryEmailChangeEmail(
        @AuthenticationPrincipal requestClient: RequestClient,
        @RequestBody dto: SendPrimaryEmailChangeEmailDTO
    ) = ok {
            userService.sendPrimaryEmailChangeEmail(requestClient.serviceRequester, dto)
    }

    @GetMapping("/session")
    fun session(@AuthenticationPrincipal requestClient: RequestClient) = ok { requestClient }
}